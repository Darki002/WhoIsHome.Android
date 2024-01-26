package ch.darki.whoishome.core

import org.joda.time.DateTime

data class RepeatEvent(val person : Person, val name : String, val startTime : DateTime,
                       val endTime : DateTime, val firstDay : DateTime, val lastDay : DateTime,
                       val relevantForDinner : Boolean, val dinnerAt : DateTime?){
}