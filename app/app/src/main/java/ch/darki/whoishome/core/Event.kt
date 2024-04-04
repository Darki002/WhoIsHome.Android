package ch.darki.whoishome.core

import com.google.firebase.firestore.DocumentSnapshot
import org.joda.time.DateTime
import java.util.Comparator

data class Event(val person: Person, val eventName: String, val date: DateTime, val startTime: DateTime,
                 val endTime: DateTime, val relevantForDinner : Boolean, val dinnerAt : DateTime?, val id : String)
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

    fun toDateTimeString() : String {
        return date.toString("dd.MM.yyyy") + " " + startTime.toString("HH:mm") + " - " + endTime.toString("HH:mm")
    }

    fun isToday() : Boolean {
        return (date.year == DateTime.now().year && date.dayOfYear == DateTime.now().dayOfYear)
    }

    fun isThisWeek() : Boolean {
        return (date.year == DateTime.now().year && date.weekOfWeekyear == DateTime.now().weekOfWeekyear)
    }

    fun toDb() : Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()

        map["id"] = id
        map["person"] = person
        map["eventName"] = eventName
        map["date"] = date.millis
        map["startTime"] = startTime.millis
        map["endTime"] = endTime.millis
        map["relevantForDinner"] = relevantForDinner
        map["dinnerAt"] = dinnerAt?.millis

        return map
    }

    companion object{
        fun create(
            person: Person,
            eventName: String,
            date: DateTime,
            startTime: DateTime,
            endTime: DateTime,
            relevantForDinner : Boolean,
            dinnerAt : DateTime?
        ) : Event {

            val docName = person.email + " - " + DateTime.now().millis

            return Event(
                person,
                eventName,
                date,
                startTime,
                endTime,
                relevantForDinner,
                dinnerAt,
                docName
            )
        }

        fun fromDb(doc : DocumentSnapshot) : Event {

            val dinnerAtMil = doc.get("dinnerAt") as? Long?
            val dateMil = doc.get("date") as? Long?
            val startTimeMil = doc.get("startTime") as? Long?
            val endTimeMil = doc.get("endTime") as? Long?

            // deprecated
            val dinnerAt =
                if (dinnerAtMil != null) {
                    DateTime(dinnerAtMil)
                }
                else {
                    convertToDateTime(doc, "dinnerAt")
                }

            val date =
                if (dateMil != null) {
                    DateTime(dateMil)
                }
                else {
                    convertToDateTime(doc, "date")
                }

            val startTime =
                if (startTimeMil != null) {
                    DateTime(startTimeMil)
                }
                else {
                    convertToDateTime(doc, "startTime")
                }

            val endTime =
                if (endTimeMil != null) {
                    DateTime(endTimeMil)
                }
                else {
                    convertToDateTime(doc, "endTime")
                }

            // deprecated
            val startDate = convertToDateTime(doc, "startDate")
            val endDate = convertToDateTime(doc, "endDate")

            return Event(
                person = Person(doc.getString("person.displayName").toString(), doc.getString("person.email").toString()),
                eventName = doc.getString("eventName").toString(),
                date= date ?: startDate ?: throw Exception("No date found"),
                startTime = startTime ?: startDate ?: throw Exception("No startTime found"),
                endTime = endTime ?: endDate ?: throw Exception("No endTime found"),
                relevantForDinner = doc.getBoolean("relevantForDinner") ?: false,
                dinnerAt = dinnerAt,
                id = doc.getString("id").toString()
            )
        }

        private fun convertToDateTime(doc: DocumentSnapshot, field: String): DateTime? {
            val nestedMap = doc[field] as? Map<*, *>

            return if (nestedMap != null && nestedMap.all { it.key is String }) {
                val year = nestedMap["year"] as? Long ?: 0
                val month = nestedMap["monthOfYear"] as? Long ?: 1
                val day = nestedMap["dayOfMonth"] as? Long ?: 1
                val hour = nestedMap["hourOfDay"] as? Long ?: 0
                val minute = nestedMap["minuteOfHour"] as? Long ?: 0
                val second = nestedMap["secondOfMinute"] as? Long ?: 0

                DateTime(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt(), second.toInt())
            } else {
                null
            }
        }
    }
}
