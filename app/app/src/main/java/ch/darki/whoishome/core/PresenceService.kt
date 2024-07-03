package ch.darki.whoishome.core

import android.util.Log
import ch.darki.whoishome.core.models.Event
import ch.darki.whoishome.core.models.Person
import ch.darki.whoishome.core.models.RepeatEvent
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.joda.time.DateTime
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.stream.Collectors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PresenceService {
    var personService : PersonService = PersonService()
        private set

    var eventService : EventService = EventService()
        private set

    var repeatEventService: RepeatEvenService = RepeatEvenService()
        private set

    fun getPresenceListFrom(scope: CoroutineScope, callback: (List<PersonPresence>) -> Unit) {
        val presenceList = ArrayList<PersonPresence>()
        val db = Firebase.firestore

        scope.launch {
            try {
                val docs = db.collection("person").get().await()

                val deferredList = docs.documents.map { doc ->
                    async {
                        suspendCoroutine { continuation ->
                            val person = Person.new(doc)
                            getPersonPresence(person) { presence ->
                                continuation.resume(presence)
                            }
                        }
                    }
                }

                presenceList.addAll(deferredList.awaitAll())
                callback.invoke(presenceList)
            } catch (e: Exception) {
                Log.e("DB Err", e.message.toString())
                Firebase.crashlytics.recordException(e)
                callback.invoke(emptyList())
            }
        }
    }

    private fun getPersonPresence(person: Person, callback : (PersonPresence) -> Unit) {
        eventService.getEventsFromPerson(person){events ->
            repeatEventService.getRepeatedEventFromPerson(person){repeatEvents ->
                calculatePersonPresence(person, events, repeatEvents, callback)
            }
        }
    }

    private fun calculatePersonPresence(person: Person, events: List<Event?>, repeatedEvents: List<RepeatEvent?>, callback : (PersonPresence) -> Unit){
        val relevantEventsToday = events.stream()
            .filter { e -> e!!.relevantForDinner }
            .filter { e -> e?.isToday() ?: false }.collect(Collectors.toList())

        val relevantRepeatedEventsToday = repeatedEvents.stream()
            .filter { e -> e!!.relevantForDinner }
            .filter { e -> e?.isToday() ?: false }.collect(Collectors.toList())

        if(relevantEventsToday.isEmpty() && relevantRepeatedEventsToday.isEmpty()){
            callback.invoke(PersonPresence(person, true, null))
            return
        }

        val eventDinnerAt = relevantEventsToday.stream().map { it?.dinnerAt }.collect(Collectors.toList())
        val repeatedDinnerAt = relevantRepeatedEventsToday.stream().map { it?.dinnerAt }.collect(Collectors.toList())

        val resultRepeatedEvent = if(repeatedDinnerAt != null){
            getLatestDateFromList(repeatedDinnerAt)
        } else{ null }

        val resultEvent = if(eventDinnerAt != null){
            getLatestDateFromList(eventDinnerAt)
        } else{ null }

        val latestDinnerAt: DateTime? = getLatestDateTime(resultEvent, resultRepeatedEvent)

        callback.invoke(PersonPresence(person, latestDinnerAt != null, latestDinnerAt))
    }

    private fun getLatestDateFromList(dateTimes: List<DateTime?>) : DateTime?{
        return dateTimes.stream()
            .filter { it != null }
            .max { o1, o2 -> o1?.compareTo(o2) ?: 1 }
            .map { it }.orElse(null)
    }

    private fun getLatestDateTime(dateTime1: DateTime?, dateTime2: DateTime?): DateTime? {
        return if (dateTime1 != null && dateTime2 != null) {
            maxOf(dateTime1, dateTime2)
        } else {
            dateTime1 ?: dateTime2
        }
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val dinnerAt: DateTime?)
}