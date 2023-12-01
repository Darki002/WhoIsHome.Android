package ch.darki.whoishome.core

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.joda.time.DateTime
import org.joda.time.LocalDate

class PresenceService {
    var personService : PersonService = PersonService()
        private set

    var eventService : EventService = EventService()
        private set

    fun getPresenceListFrom(callback: (List<PersonPresence>) -> Unit) {
        val presenceList = ArrayList<PersonPresence>()
        val db = Firebase.firestore

        db.collection("person").get()
            .addOnSuccessListener {
                it.documents.forEach { d ->
                    val person = Person.new(d)
                    getPersonPresence(person){ presence ->
                        presenceList.add(presence)
                    }
                }

                callback.invoke(presenceList)
            }
            .addOnFailureListener {
                Log.e("DB Err", it.message.toString())
                callback.invoke(arrayListOf())
            }
    }

    private fun getPersonPresence(person: Person, callback : (PersonPresence) -> Unit) {
        eventService.getEventsFromPerson(person){events ->
            val now = DateTime.now()

            val today: LocalDate = now.toLocalDate()
            val tomorrow: LocalDate = today.plusDays(1)

            val startOfToday: DateTime = today.toDateTimeAtStartOfDay(now.zone)
            val startOfTomorrow: DateTime = tomorrow.toDateTimeAtStartOfDay(now.zone)

            val result = events.stream().filter {
                    e -> e!!.relevantForDinner
            }.filter {
                    e -> (e!!.dinnerAt != null && startOfToday <= e.dinnerAt && e.dinnerAt!! < startOfTomorrow)
            }.max{
                    e, e2 -> e!!.dinnerAt?.compareTo(e2!!.dinnerAt) ?: -1
            }.map { it?.dinnerAt }.orElse(null)

            callback.invoke(PersonPresence(person, result != null, result))
        }
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val dinnerAt: DateTime?)

}