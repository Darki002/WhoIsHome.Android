package ch.darki.whoishome.core

import android.app.Application
import androidx.annotation.RequiresApi
import org.joda.time.DateTime
import kotlin.jvm.optionals.getOrNull

class EventService {
    var events : List<Event>? = null
        private set

    fun loadAllEvents(){
        events = listOf(
            Event(
                Person("Llyn", "llyn.baumann@gmx.ch"),
                "GYM",
                DateTime.now().withTime(17, 0, 0, 0),
                DateTime.now().withTime(18, 0, 0, 0)
            ),
            Event(
                Person("Ryanne", "ryanne@gmx.ch"),
                "GYM",
                DateTime.now().plusDays(1).withTime(17, 0, 0, 0),
                DateTime.now().plusDays(1).withTime(18, 0, 0, 0)
            ),
            Event(
                Person("Jennifer", "jennifer.baumann@bluewin.ch"),
                "Chlätere",
                DateTime.now().plusDays(2).withTime(18, 0, 0, 0),
                DateTime.now().plusDays(2).withTime(20, 0, 0, 0)
            )
        )
    }

    fun getEventsForPersonByEmail(email : String) : EventsForPerson {

        val now = DateTime.now()

        val personEvents = events?.stream()?.filter {
            e -> e.person.email == email
        }

        val today = personEvents?.filter {
                e -> e.startDate.dayOfYear() == now.dayOfYear()
        }?.toList()

        val thisWeek = events?.stream()?.filter {
                e -> e.person.email == email
        }?.filter {
                e -> (e.startDate.monthOfYear() == now.monthOfYear() && e.startDate.weekOfWeekyear() == now.weekOfWeekyear() && e.startDate.dayOfWeek().get() > now.dayOfWeek().get())
        }?.toList()

        return EventsForPerson(today ?: ArrayList(), thisWeek ?: ArrayList())

    }

    data class EventsForPerson(val today : List<Event>, val thisWeek : List<Event>)
}