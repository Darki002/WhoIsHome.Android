package ch.darki.whoishome.core

import org.joda.time.DateTime

data class RepeatEvent(val person : Person, val name : String, val startTime : DateTime,
                       val endTime : DateTime, val firstDay : DateTime, val lastDay : DateTime,
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
        map["relevantForDinner"] = relevantForDinner
        map["dinnerAt"] = dinnerAt?.millis

        return map
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

            return RepeatEvent(
                id = id,
                person = person,
                name = name,
                startTime = startTime,
                endTime = endTime,
                firstDay = firstDay,
                lastDay = lastDay,
                relevantForDinner = relevantForDinner,
                dinnerAt = dinnerAt
            )
        }
    }
}