package ch.darki.whoishome.core

import org.joda.time.DateTime
import java.util.Comparator

data class Event(val id : Int, val person: Person, val eventName: String, val startDate: DateTime, val endDate: DateTime? = null) : Comparator<Event> {

    fun hasEndDate() : Boolean{
        return endDate != null
    }

    override fun compare(p0: Event?, p1: Event?): Int {

        if(p0 == p1){
            return 0
        }
        if(p0 == null || p1 == null){
            return 0
        }

        return p0.startDate.compareTo(p1.startDate)
    }

}
