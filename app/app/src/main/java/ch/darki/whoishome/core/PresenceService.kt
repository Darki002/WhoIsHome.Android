package ch.darki.whoishome.core

import org.joda.time.DateTime

class PresenceService {

    companion object{
        var instance : PresenceService? = null
            private set

        fun new(){
            instance = PresenceService()
        }
    }


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