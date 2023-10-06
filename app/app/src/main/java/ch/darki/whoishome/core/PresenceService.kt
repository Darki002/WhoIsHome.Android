package ch.darki.whoishome.core

import android.app.Application
import org.joda.time.DateTime
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

        personService.persons?.forEach(
            fun(person){
                val isPresent = eventService.events?.stream()?.filter{
                    e -> e.person.email.lowercase() == person.email.lowercase()
                }?.filter {
                    e -> e.endDate.year == dateTime.year && e.endDate.dayOfYear == dateTime.dayOfYear
                }?.filter {
                    isEventAtDinnerTime(it)
                }?.toArray()?.isEmpty() ?: true

                val lastEvent = eventService.events?.stream()?.filter {
                        e -> e.person.email.lowercase() == person.email.lowercase()
                }?.filter {
                        e -> e.endDate.year == dateTime.year && e.endDate.dayOfYear == dateTime.dayOfYear
                }?.max { p, c -> p.endDate.compareTo(c.endDate) }?.getOrNull()

                val personPresence = PersonPresence(person, isPresent, lastEvent)
                presenceList.add(personPresence)
            }
        )
        return presenceList
    }

    private fun isEventAtDinnerTime(e : Event) : Boolean{
        if(e.endDate.hourOfDay >= 19){
            return true
        }
        return false
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val lastEvent: Event?)

}