package ch.darki.whoishome.core

import org.joda.time.DateTime

class EventService {

    var events: ArrayList<Event>? = null
        private set

    fun loadAllEvents() {
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
                "Auto Fahre",
                DateTime.now().plusDays(1).withTime(18, 0, 0, 0),
                DateTime.now().plusDays(1).withTime(19, 0, 0, 0)
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

    private fun getNewId(): Int {
        var curId = 0

        events?.forEach { e ->
            if (e.id > curId) {
                curId = e.id
            }
        }

        return curId + 1
    }

    fun deleteEvent(id: Int) {
        events?.removeIf { e ->
            e.id == id
        }
    }

    fun createEvent(
        person: Person,
        eventName: String,
        startDate: DateTime,
        endDate: DateTime
    ) {

        val event = Event(
            getNewId(),
            person,
            eventName,
            startDate,
            endDate
        )
        events?.add(event)
    }

    fun getEventsForPersonByEmail(email: String): EventsForPerson {

        val now = DateTime.now()

        val todaysEvents = events?.stream()?.filter { e ->
            e.person.email.lowercase() == email.lowercase()
        }?.filter { e ->
            e.startDate.year == now.year &&
            e.startDate.dayOfYear() == now.dayOfYear()
        }?.toArray<Event> { arrayOfNulls<Event>(it) }

        val thisWeeksEvents = events?.stream()?.filter { e ->
            e.person.email.lowercase() == email.lowercase()
        }?.filter { e -> (
                    e.startDate.year == now.year &&
                    e.startDate.weekOfWeekyear == now.weekOfWeekyear &&
                    e.startDate.dayOfWeek > now.dayOfWeek)
        }?.toArray<Event> { arrayOfNulls<Event>(it) }

        val endOfWeek = DateTime.now().withDayOfWeek(7)
        val otherEvents = events?.stream()?.filter { e ->
            e.person.email.lowercase() == email.lowercase()
        }?.filter { e ->
            e.startDate > endOfWeek
        }?.toArray<Event> { arrayOfNulls<Event>(it) }

        return EventsForPerson(
            todaysEvents?.asList() ?: ArrayList(),
            thisWeeksEvents?.asList() ?: ArrayList(),
            otherEvents?.asList() ?: ArrayList()
        )
    }

    data class EventsForPerson(
        val today: List<Event>,
        val thisWeek: List<Event>,
        val otherEvents: List<Event>
    )
}