package ch.darki.whoishome.core

import org.joda.time.DateTime

class PresenceService {

    private val personService : PersonService = PersonService()
    private val eventService : EventService = EventService()

    init {
        personService.loadAllPersons()
        eventService.loadAllEvents()
    }

    fun getPresenceListFrom(dateTime: DateTime) : List<PersonPresence> {
        val presenceList = ArrayList<PersonPresence>()

        personService.persons?.forEach(
            fun(person){
                val isPresent = eventService.events?.stream()?.filter{
                    e -> e.person.email == person.email
                }?.filter {
                    e -> e.startDate.dayOfYear() == dateTime.dayOfYear()
                }?.toArray()?.isEmpty() ?: true

                val personPresence = PersonPresence(person, isPresent)
                presenceList.add(personPresence)
            }
        )
        return presenceList
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean)

}