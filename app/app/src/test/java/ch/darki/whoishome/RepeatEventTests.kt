package ch.darki.whoishome

import ch.darki.whoishome.core.Person
import ch.darki.whoishome.core.RepeatEvent
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Test

class RepeatEventTests {
    @Test
    fun create_returnsNewInstance_WithDatesEveryWeek() {
        val expected = 5

        val person = Person("Test", "test@gmail.com")
        val testee = RepeatEvent.create(
            person = person,
            name = "Test Event",
            dinnerAt = null,
            relevantForDinner = false,
            firstDay = DateTime(2024, 5, 1, 0, 0, 0),
            lastDay = DateTime(2024, 5, 31, 0, 0, 0),
            startTime = DateTime(0, 1, 1, 18, 0, 0),
            endTime = DateTime(0, 1, 1, 19, 0, 0)
        )

        Assert.assertEquals(expected, testee.dates.count())
    }
}