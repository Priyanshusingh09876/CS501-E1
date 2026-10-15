package com.priyanshusingh.campuseventexplorer.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EventRepositoryTest {

    @Test
    fun `repository contains at least eight events`() {
        assertTrue("Expected >= 8 events, got ${EventRepository.events.size}", EventRepository.events.size >= 8)
    }

    @Test
    fun `every event id is unique`() {
        val ids = EventRepository.events.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `every event has non-blank fields`() {
        EventRepository.events.forEach { event ->
            assertTrue("title blank for id ${event.id}", event.title.isNotBlank())
            assertTrue("category blank for id ${event.id}", event.category.isNotBlank())
            assertTrue("location blank for id ${event.id}", event.location.isNotBlank())
            assertTrue("time blank for id ${event.id}", event.time.isNotBlank())
            assertTrue("description blank for id ${event.id}", event.description.isNotBlank())
        }
    }

    @Test
    fun `findById returns the matching event`() {
        val expected = EventRepository.events[2]
        assertEquals(expected, EventRepository.findById(expected.id))
    }

    @Test
    fun `findById returns null for unknown, negative and extreme ids`() {
        assertNull(EventRepository.findById(999_999L))
        assertNull(EventRepository.findById(-1L))
        assertNull(EventRepository.findById(0L))
        assertNull(EventRepository.findById(Long.MIN_VALUE))
        assertNull(EventRepository.findById(Long.MAX_VALUE))
    }

    @Test
    fun `categories are distinct and cover every event`() {
        val categories = EventRepository.categories
        assertEquals(categories.size, categories.toSet().size)
        EventRepository.events.forEach { assertTrue(it.category in categories) }
    }

    @Test
    fun `the assignment sample event is present`() {
        val sample = EventRepository.events.first { it.title == "Mobile Development Meetup" }
        assertEquals("Tuesday 6:30 PM", sample.time)
        assertEquals("CAS 214", sample.location)
    }
}
