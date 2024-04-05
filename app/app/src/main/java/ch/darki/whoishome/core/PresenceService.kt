package ch.darki.whoishome.core

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.joda.time.DateTime
import org.joda.time.LocalDate
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.streams.toList

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

                // Use async to parallelize the asynchronous calls
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
                callback.invoke(emptyList())
            }
        }
    }

    private fun getPersonPresence(person: Person, callback : (PersonPresence) -> Unit) {
        eventService.getEventsFromPerson(person){events ->

            val relevantEventsToday = events.stream()
                .filter { e -> e!!.relevantForDinner }
                .filter { e -> e?.isToday() ?: false }.toList()

            if(relevantEventsToday.isEmpty()){
                callback.invoke(PersonPresence(person, true, null))
                return@getEventsFromPerson
            }

            val result = relevantEventsToday.stream().max{
                    e, e2 -> e?.dinnerAt?.compareTo(e2?.dinnerAt) ?: 1
            }.map { it?.dinnerAt }.orElse(null)

            callback.invoke(PersonPresence(person, result != null, result))
        }
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val dinnerAt: DateTime?)
}