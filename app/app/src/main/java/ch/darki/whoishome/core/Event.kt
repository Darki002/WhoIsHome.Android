package ch.darki.whoishome.core

import org.joda.time.DateTime

data class Event(val id : Int, val person: Person, val eventName: String, val startDate: DateTime, val endDate: DateTime? = null)
