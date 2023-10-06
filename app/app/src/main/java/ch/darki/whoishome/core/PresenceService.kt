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
                    e -> e.startDate.year == dateTime.year && e.startDate.dayOfYear == dateTime.dayOfYear
                }?.filter {
                    isEventAtDinnerTime(it)
                }?.toArray()?.isEmpty() ?: true

                val lastEvent = eventService.events?.stream()?.filter {
                        e -> e.person.email.lowercase() == person.email.lowercase()
                }?.filter {
                        e -> e.startDate.year == dateTime.year && e.startDate.dayOfYear == dateTime.dayOfYear
                }?.max { p, c -> p.startDate.compareTo(c.startDate) }?.getOrNull()

                val personPresence = PersonPresence(person, isPresent, lastEvent)
                presenceList.add(personPresence)
            }
        )
        return presenceList
    }

    private fun isEventAtDinnerTime(e : Event) : Boolean{
        if(e.hasEndDate()){
            if(e.endDate!!.hourOfDay >= 19){
                return false
            }
            return true
        }
        else{
            if(e.startDate.hourOfDay >= 18){
                return false
            }
            return true
        }
    }

    data class PersonPresence(val person: Person, val isPresent : Boolean, val lastEvent: Event?)

}