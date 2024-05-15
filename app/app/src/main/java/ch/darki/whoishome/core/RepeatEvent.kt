package ch.darki.whoishome.core

import com.google.firebase.firestore.DocumentSnapshot
import org.joda.time.DateTime

data class RepeatEvent(val person : Person, val name : String, val startTime : DateTime,
                       val endTime : DateTime, val firstDay : DateTime, val lastDay : DateTime, val dates: List<DateTime>,
                       val relevantForDinner : Boolean, val dinnerAt : DateTime?, val id : String){

    fun toDb() : Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()

        map["id"] = id
        map["person"] = person
        map["name"] = name
        map["startTime"] = startTime.millis
        map["endTime"] = endTime.millis
        map["firstDay"] = firstDay.millis
        map["lastDay"] = lastDay.millis
        map["dates"] = dates.map { it.millis }
        map["relevantForDinner"] = relevantForDinner
        map["dinnerAt"] = dinnerAt?.millis

        return map
    }

    fun toDateTimeString() : String {
        val now = DateTime.now()
        for (date in dates){
            if(date.year == now.year && date.dayOfYear == now.dayOfYear){
                date.toString("dd.MM.yyyy") + " " + startTime.toString("HH:mm") + " - " + endTime.toString("HH:mm")
            }
        }
        return "404 Not Found :("
    }

    fun isToday() : Boolean {
        val now = DateTime.now()
        for (date in dates){
            if(date.year == now.year && date.dayOfYear == now.dayOfYear){
                return true
            }
        }
        return false
    }

    fun isThisWeek() : Boolean {
        val now = DateTime.now()
        for (date in dates){
            if(date.year == now.year && date.weekOfWeekyear == now.weekOfWeekyear){
                return true
            }
        }
        return false
    }

    companion object {
        fun create(
            person: Person,
            name: String,
            startTime: DateTime,
            endTime: DateTime,
            firstDay: DateTime,
            lastDay: DateTime,
            relevantForDinner: Boolean,
            dinnerAt: DateTime?
        ) : RepeatEvent
        {
            val id = person.email + " - " + DateTime.now().millis

            val dates = ArrayList<DateTime>()
            var current = firstDay
            while (current < endTime){
                dates.add(current)
                current = current.withDayOfMonth(current.dayOfMonth + 7)
            }

            return RepeatEvent(
                id = id,
                person = person,
                name = name,
                startTime = startTime,
                endTime = endTime,
                firstDay = firstDay,
                lastDay = lastDay,
                dates = dates,
                relevantForDinner = relevantForDinner,
                dinnerAt = dinnerAt
            )
        }

        fun fromDb(doc : DocumentSnapshot) : RepeatEvent {
            val startTimeMil = doc.get("startTime") as? Long?
            val endTimeMil = doc.get("endTime") as? Long?
            val firstDayMil = doc.get("firstDay") as? Long?
            val lastDayMil = doc.get("lastDay") as? Long?
            val dinnerAtMil = doc.get("dinnerAt") as? Long?

            val dateMillisList = doc.get("dates") as? List<Long> ?: emptyList()
            val dates = dateMillisList.map { DateTime(it) }

            return RepeatEvent(
                id = doc.get("id").toString(),
                person = Person(doc.getString("person.displayName").toString(), doc.getString("person.email").toString()),
                name = doc.getString("eventName").toString(),
                startTime = DateTime(startTimeMil),
                endTime = DateTime(endTimeMil),
                firstDay = DateTime(firstDayMil),
                lastDay = DateTime(lastDayMil),
                dates = dates,
                relevantForDinner = doc.getBoolean("relevantForDinner") ?: false,
                dinnerAt = DateTime(dinnerAtMil)
            )
        }
    }
}