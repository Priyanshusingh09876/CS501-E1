package com.priyanshusingh.campuseventexplorer.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class EventFilterTest {

    private val events = listOf(
        CampusEvent(1, "Mobile Development Meetup", "Tech", "CAS 214", "Tuesday 6:30 PM", "d"),
        CampusEvent(2, "Fall Career Fair", "Career", "GSU", "Wednesday 11:00 AM", "d"),
        CampusEvent(3, "Yoga on the Esplanade", "Wellness", "Esplanade", "Saturday 9:00 AM", "d"),
        CampusEvent(4, "Hack the Charles", "Tech", "Questrom", "Saturday 10:00 AM", "d"),
    )

    @Test
    fun `blank query returns every event unchanged`() {
        assertSame(events, events.filterByQuery(""))
        assertSame(events, events.filterByQuery("   "))
        assertSame(events, events.filterByQuery("\n\t"))
    }

    @Test
    fun `matches on title substring`() {
        assertEquals(listOf(2L), events.filterByQuery("career fair").ids())
        assertEquals(listOf(1L), events.filterByQuery("Meetup").ids())
    }

    @Test
    fun `matches on category substring`() {
        assertEquals(listOf(1L, 4L), events.filterByQuery("Tech").ids())
        assertEquals(listOf(3L), events.filterByQuery("well").ids())
    }

    @Test
    fun `matching is case-insensitive`() {
        assertEquals(listOf(1L, 4L), events.filterByQuery("tEcH").ids())
        assertEquals(listOf(2L), events.filterByQuery("FALL CAREER").ids())
    }

    @Test
    fun `leading, trailing and repeated whitespace are ignored`() {
        assertEquals(listOf(2L), events.filterByQuery("   career    fair  ").ids())
    }

    @Test
    fun `does not match on location or time`() {
        assertTrue(events.filterByQuery("CAS 214").isEmpty())
        assertTrue(events.filterByQuery("Saturday").isEmpty())
    }

    @Test
    fun `no match returns an empty list instead of throwing`() {
        assertEquals(emptyList<CampusEvent>(), events.filterByQuery("quantum physics"))
    }

    @Test
    fun `original order is preserved`() {
        assertEquals(listOf(1L, 4L), events.filterByQuery("e").filter { it.category == "Tech" }.ids())
    }

    @Test
    fun `filtering an empty collection is safe`() {
        assertEquals(emptyList<CampusEvent>(), emptyList<CampusEvent>().filterByQuery("tech"))
    }

    @Test
    fun `filtering is a pure function of its inputs`() {
        val first = events.filterByQuery("tech")
        val second = events.filterByQuery("tech")
        assertEquals(first, second)
        assertEquals(4, events.size) // source untouched
    }

    @Test
    fun `normalize collapses whitespace`() {
        assertEquals("career fair", EventFilter.normalize("  career \n  fair\t"))
        assertEquals("", EventFilter.normalize("   "))
    }

    private fun List<CampusEvent>.ids() = map { it.id }
}
