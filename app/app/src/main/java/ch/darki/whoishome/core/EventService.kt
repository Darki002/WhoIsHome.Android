package ch.darki.whoishome.core

import org.joda.time.DateTime

class EventService {
    var events : List<Event>? = null
        private set

    fun loadAllEvents(){
        events = listOf(
            Event(
                Person("Llyn", "Baumann", "llyn.baumann@gmx.ch"),
                "GYM",
                DateTime.now().withTime(17, 0, 0, 0),
                DateTime.now().withTime(18, 0, 0, 0)
            ),
            Event(
                Person("Ryanne", "Baumann", "ryanne@gmx.ch"),
                "GYM",
                DateTime.now().plusDays(1).withTime(17, 0, 0, 0),
                DateTime.now().plusDays(1).withTime(18, 0, 0, 0)
            ),
            Event(
                Person("Jennifer", "Baumann", "jennifer.baumann@bluewin.ch"),
                "Chlätere",
                DateTime.now().plusDays(2).withTime(18, 0, 0, 0),
                DateTime.now().plusDays(2).withTime(20, 0, 0, 0)
            )
        )
    }
}