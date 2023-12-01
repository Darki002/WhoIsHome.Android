package ch.darki.whoishome.core

import com.google.firebase.firestore.DocumentSnapshot
import org.joda.time.DateTime
import java.util.Comparator

data class Event(val person: Person, val eventName: String, val startDate: DateTime,
                 val endDate: DateTime, val relevantForDinner : Boolean, val dinnerAt : DateTime?, val id : String)
    : Comparator<Event> {

    override fun compare(p0: Event?, p1: Event?): Int {

        if(p0 == p1){
            return 0
        }
        if(p0 == null || p1 == null){
            return 0
        }

        if(dinnerAt == null){
            return -1
        }

        return p0.dinnerAt!!.compareTo(p1.dinnerAt)
    }

    companion object{
        fun new(doc : DocumentSnapshot) : Event {

            val dinnerAt = doc.getDocumentReference("dinnerAt")?.get()?.result?.toObject(DateTime::class.java)
            val startDate = doc.getDocumentReference("startDate")?.get()?.result?.toObject(DateTime::class.java)
            val endDate = doc.getDocumentReference("endDate")?.get()?.result?.toObject(DateTime::class.java)

            return Event(
                person = doc.getDocumentReference("person")
                    ?.get()?.result?.toObject(Person::class.java)
                    ?: throw Exception("event does not have a person"),
                eventName = doc.getString("eventName").toString(),
                startDate = startDate!!,
                endDate = endDate!!,
                relevantForDinner = doc.getBoolean("relevantForDinner") ?: false,
                dinnerAt = dinnerAt,
                id = doc.getString("id").toString()
            )
        }
    }
}
