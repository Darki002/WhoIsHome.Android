package ch.darki.whoishome.core

import org.joda.time.DateTime
import org.joda.time.LocalDate
import kotlin.jvm.optionals.getOrNull

class PresenceService {
    var personService : PersonService = PersonService()
        private set

    var eventService : EventService = EventService()
        private set

    init {
        personService.loadAllPersons()
        eventService.loadAllEvents()
    }

    fun getPresenceListFrom(dateTime: DateTime) : List<PersonPresence> {
        val presenceList = ArrayList<PersonPresence>()

        personService.persons?.forEach {
            val presence = getPersonPresence(it)
            presenceList.add(presence)
        }
        return presenceList
    }

    private fun getPersonPresence(person: Person) : PersonPresence {
        val events = eventService.getEventsFromPerson(person)

        var now = DateTime.now()

        val today: LocalDate = now.toLocalDate()
        val tomorrow: LocalDate = today.plusDays(1)

        val startOfToday: DateTime = today.toDateTimeAtStartOfDay(now.zone)
        val startOfTomorrow: DateTime = tomorrow.toDateTimeAtStartOfDay(now.zone)

        val result = events.stream().filter {
            e -> e.relevantForDinner
        }.filter {
            e -> (e.dinnerAt != null && startOfToday <= e.dinnerAt && e.dinnerAt < startOfTomorrow)
        }.max{
            e, e2 -> e.dinnerAt?.compareTo(e2.dinnerAt) ?: -1
        }.getOrNull()?.dinnerAt

        return PersonPresence(person, result != null, result)
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val dinnerAt: DateTime?)

}