package ch.darki.whoishome.core

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
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
        fun new(doc : DocumentSnapshot) : Event? {

            val dinnerAt = convertToDateTime(doc, "dinnerAt")
            val startDate = convertToDateTime(doc, "startDate")
            if (startDate == null) {
                Log.e("EventConversion", "Invalid Event in DB: StartDate is null")
                return null
            }

            val endDate = convertToDateTime(doc, "endDate")
            if (endDate == null) {
                Log.e("EventConversion", "Invalid Event in DB: EndDate is null")
                return null
            }

            return Event(
                person = Person(doc.getString("person.displayName").toString(), doc.getString("person.email").toString()),
                eventName = doc.getString("eventName").toString(),
                startDate = startDate,
                endDate = endDate,
                relevantForDinner = doc.getBoolean("relevantForDinner") ?: false,
                dinnerAt = dinnerAt,
                id = doc.getString("id").toString()
            )
        }

        private fun convertToDateTime(doc: DocumentSnapshot, field: String): DateTime? {
            val nestedMap = doc[field] as? Map<*, *>

            return if (nestedMap != null && nestedMap.all { it.key is String }) {
                val year = nestedMap["year"] as? Int ?: 0
                val month = nestedMap["monthOfYear"] as? Int ?: 1
                val day = nestedMap["dayOfMonth"] as? Int ?: 1
                val hour = nestedMap["hourOfDay"] as? Int ?: 0
                val minute = nestedMap["minuteOfHour"] as? Int ?: 0
                val second = nestedMap["secondOfMinute"] as? Int ?: 0

                DateTime(year, month, day, hour, minute, second)
            } else {
                null
            }
        }
    }
}
