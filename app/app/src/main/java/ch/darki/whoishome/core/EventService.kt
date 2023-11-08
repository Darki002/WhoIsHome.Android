package ch.darki.whoishome.core

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.joda.time.DateTime

class EventService {

    fun deleteEvent(id: Int) {
        Firebase.firestore.collection("events")
    }

    fun createEvent(
        person: Person,
        eventName: String,
        startDate: DateTime,
        endDate: DateTime,
        relevantForDinner : Boolean,
        dinnerAt : DateTime?,
        callback: (Boolean) -> Unit
    ) {

        val event = Event(
            getNewId(),
            person,
            eventName,
            startDate,
            endDate,
            relevantForDinner,
            dinnerAt
        )

        val docName = person.email + " - " + DateTime.now().millis

        Firebase.firestore.collection("events").document(docName).set(event)
            .addOnSuccessListener {callback.invoke(true)}
            .addOnFailureListener {callback.invoke(false)}
    }

    fun getEventsFromPerson(person: Person, callback: (List<Event?>) -> Unit) {

        val result = ArrayList<Event?>()
        val db = Firebase.firestore

        db.collection("events")
            .whereEqualTo("email", person.email).get()
            .addOnFailureListener {
                callback.invoke(listOf())
            }
            .addOnSuccessListener {
                it.documents.forEach { e ->
                    val event = e.toObject(Event::class.java)
                    result.add(event)
                }
                callback.invoke(result)
            }
    }

    fun getEventsForPersonByEmail(email: String, callback: (EventsForPerson) -> Unit) {

        val eventsByPerson = Firebase.firestore.collection("events")
            .whereEqualTo(FieldPath.of("person", "email"), email).get()
            .addOnSuccessListener {
                val events = arrayListOf<Event?>()
                it.documents.forEach { e ->
                    val event = e.toObject(Event::class.java)
                    events.add(event)
                }
                val result = getPresences(events)
                callback.invoke(result)
            }


    }

    private fun getPresences(events: ArrayList<Event?>) : EventsForPerson{
        val now = DateTime.now()

        val todaysEvents = events.stream().filter { e ->
            e?.startDate?.year == now.year &&
                    e.startDate.dayOfYear() == now.dayOfYear()
        }?.toArray<Event> { arrayOfNulls<Event>(it) }

        val thisWeeksEvents = events.stream().filter { e -> (
                e?.startDate?.year == now.year &&
                        e.startDate.weekOfWeekyear == now.weekOfWeekyear &&
                        e.startDate.dayOfWeek > now.dayOfWeek)
        }?.toArray<Event> { arrayOfNulls<Event>(it) }

        val endOfWeek = DateTime.now().withDayOfWeek(7)
        val otherEvents = events.stream().filter { e ->
            e != null && e.startDate > endOfWeek
        }?.toArray<Event> { arrayOfNulls<Event>(it) }

        return EventsForPerson(
            todaysEvents?.asList() ?: ArrayList(),
            thisWeeksEvents?.asList() ?: ArrayList(),
            otherEvents?.asList() ?: ArrayList()
        )
    }

    data class EventsForPerson(
        val today: List<Event>,
        val thisWeek: List<Event>,
        val otherEvents: List<Event>
    )
}