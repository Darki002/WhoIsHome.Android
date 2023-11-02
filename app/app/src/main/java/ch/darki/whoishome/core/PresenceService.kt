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
                
            }
        )
        return presenceList
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val dinnerAt: DateTime?)

}