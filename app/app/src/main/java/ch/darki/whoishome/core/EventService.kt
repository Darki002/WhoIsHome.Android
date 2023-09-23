package ch.darki.whoishome.core

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
}