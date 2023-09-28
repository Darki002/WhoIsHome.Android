package ch.darki.whoishome.core

import android.system.Os.remove
import org.joda.time.DateTime

class EventService {
    var events : ArrayList<Event>? = null
        private set

    fun loadAllEvents(){
        events = arrayListOf(
            Event(
                1,
                Person("Llyn", "llyn.baumann@gmx.ch"),
                "GYM",
                DateTime.now().withTime(17, 0, 0, 0),
                DateTime.now().withTime(18, 0, 0, 0)
            ),
            Event(
                2,
                Person("Llyn", "llyn.baumann@gmx.ch"),
                "GYM",
                DateTime.now().plusDays(1).withTime(17, 0, 0, 0),
                DateTime.now().plusDays(1).withTime(18, 0, 0, 0)
            ),
            Event(
                3,
                Person("Ryanne", "ryanne@gmx.ch"),
                "GYM",
                DateTime.now().plusDays(1).withTime(17, 0, 0, 0),
                DateTime.now().plusDays(1).withTime(18, 0, 0, 0)
            ),
            Event(
                4,
                Person("Jennifer", "jennifer.baumann@bluewin.ch"),
                "Chlätere",
                DateTime.now().plusDays(2).withTime(18, 0, 0, 0),
                DateTime.now().plusDays(2).withTime(20, 0, 0, 0)
            )
        )
    }

    fun deleteEvent(id : Int){
        events?.removeIf {
                e -> e.id == id
        }
    }

    fun getEventsForPersonByEmail(email : String) : EventsForPerson {

        val now = DateTime.now()

        val personEvents = events?.stream()?.filter {
            e -> e.person.email == email
        }

        val todaysEvents = personEvents?.filter {
                e -> e.startDate.dayOfYear() == now.dayOfYear()
        }?.toArray<Event> {arrayOfNulls<Event>(it) }

        val thisWeeksEvents = events?.stream()?.filter {
                e -> e.person.email == email
        }?.filter {
                e -> (e.startDate.monthOfYear() == now.monthOfYear() && e.startDate.weekOfWeekyear() == now.weekOfWeekyear() && e.startDate.dayOfWeek().get() > now.dayOfWeek().get())
        }?.toArray<Event> {arrayOfNulls<Event>(it) }

        return EventsForPerson(todaysEvents?.asList() ?: ArrayList(), thisWeeksEvents?.asList() ?: ArrayList())
    }

    data class EventsForPerson(val today : List<Event>, val thisWeek : List<Event>)
}