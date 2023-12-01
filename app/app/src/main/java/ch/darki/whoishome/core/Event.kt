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
        fun New(doc : DocumentSnapshot) : Event {

            val personDoc = doc.getDocumentReference("person")?.get()?.result!!
            val dinnerAtString = doc.getString("dinnerAt")
            val dinnerAt = if (dinnerAtString.isNullOrEmpty()) null else DateTime(dinnerAtString)

            return Event(
                person = Person.new(personDoc),
                eventName = doc.getString("eventName").toString(),
                startDate = DateTime(doc.getString("startDate").toString()),
                endDate = DateTime(doc.getString("endDate").toString()),
                relevantForDinner = doc.getBoolean("relevantForDinner") ?: false,
                dinnerAt = dinnerAt,
                id = doc.getString("id").toString()
            )
        }
    }
}
