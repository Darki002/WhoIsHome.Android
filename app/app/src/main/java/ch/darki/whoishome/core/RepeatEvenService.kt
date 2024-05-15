package ch.darki.whoishome.core

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RepeatEvenService {

    private val collection = "repeated-events"

    fun createRepeatedEvent(
        person: Person,
        name: String,
        startTime: DateTime,
        endTime: DateTime,
        firstDay: DateTime,
        lastDay: DateTime,
        relevantForDinner: Boolean,
        dinnerAt: DateTime?,
        callback: (Boolean) -> Unit
    ){
        val repeatEvent = RepeatEvent.create(
            person = person,
            name = name,
            startTime = startTime,
            endTime = endTime,
            firstDay = firstDay,
            lastDay = lastDay,
            relevantForDinner = relevantForDinner,
            dinnerAt = dinnerAt
        )

        Firebase.firestore.collection(collection).document(repeatEvent.id).set(repeatEvent.toDb())
            .addOnSuccessListener {
                callback.invoke(true)
            }
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                callback.invoke(false)
            }
    }

    fun getRepeatedEventFromPerson(person: Person, callback: (List<RepeatEvent?>) -> Unit) {
        val result = ArrayList<RepeatEvent?>()
        val db = Firebase.firestore

        db.collection(collection)
            .whereEqualTo(FieldPath.of("person", "email"), person.email).get()
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                callback.invoke(listOf())
            }
            .addOnSuccessListener {
                for (doc in it.documents){
                    result.add(RepeatEvent.fromDb(doc))
                }
                callback.invoke(result)
            }
    }

    fun getAllRepeatedEventsFromPerson(scope: CoroutineScope, email: String, callback: (RepeatedEventsForPerson) -> Unit) {
        scope.launch {
            try {
                val docs = Firebase.firestore.collection(collection)
                    .whereEqualTo(FieldPath.of("person", "email"), email).get().await()

                val deferredList = docs.documents.map { doc ->
                    async {
                        suspendCoroutine { continuation ->
                            val event = RepeatEvent.fromDb(doc)
                            continuation.resume(event)
                        }
                    }
                }
                val result = getPresences(deferredList.awaitAll())
                callback.invoke(result)
            }
            catch (e: Exception){
                Log.e("DB Err", e.message.toString())
            }
        }
    }

    private fun getPresences(events: List<RepeatEvent?>) : RepeatedEventsForPerson {

        val todaysEvents = events.stream()
            .filter { e -> e?.isToday() ?: false }
            ?.toArray<RepeatEvent> { arrayOfNulls<RepeatEvent>(it) }

        val thisWeeksEvents = events.stream()
            .filter { e -> e != null && e.hasRelevantDates() }
            .filter { e -> ((e!!.isThisWeek() && e.nextDateFromToday()!!.dayOfWeek > DateTime.now().dayOfWeek)) }
            ?.toArray<RepeatEvent> { arrayOfNulls<RepeatEvent>(it) }

        val otherEvents = events.stream()
            .filter { e -> e != null && e.hasRelevantDates() }
            .filter { e -> !e!!.isToday() && !e.isThisWeek() }
            ?.toArray<RepeatEvent> { arrayOfNulls<RepeatEvent>(it) }

        return RepeatedEventsForPerson(
            todaysEvents?.asList() ?: ArrayList(),
            thisWeeksEvents?.asList() ?: ArrayList(),
            otherEvents?.asList() ?: ArrayList()
        )
    }

    data class RepeatedEventsForPerson(
        val today: List<RepeatEvent>,
        val thisWeek: List<RepeatEvent>,
        val otherEvents: List<RepeatEvent>
    )
}