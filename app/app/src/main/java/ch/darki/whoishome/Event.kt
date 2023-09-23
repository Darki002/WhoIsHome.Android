package ch.darki.whoishome

import org.joda.time.DateTime

data class Event(val person: Person, val eventName: String, val startDate: DateTime, val endDate: DateTime? = null)
