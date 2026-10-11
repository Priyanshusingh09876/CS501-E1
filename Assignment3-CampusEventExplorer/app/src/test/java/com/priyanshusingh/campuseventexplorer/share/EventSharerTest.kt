package com.priyanshusingh.campuseventexplorer.share

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pure logic tests for the share text plus Robolectric-backed checks of the intent itself
 * (`android.content.Intent` needs a real implementation, which Robolectric provides).
 */
@RunWith(AndroidJUnit4::class)
class EventSharerTest {

    private val event = CampusEvent(
        id = 1,
        title = "Mobile Development Meetup",
        category = "Tech",
        location = "CAS 214",
        time = "Tuesday 6:30 PM",
        description = "Monthly meetup for students building Android and iOS apps.",
    )

    @Test
    fun `summary line follows title - time - location contract`() {
        assertEquals(
            "Mobile Development Meetup \u2014 Tuesday 6:30 PM \u2014 CAS 214",
            EventSharer.buildSummaryLine(event),
        )
    }

    @Test
    fun `share text starts with the summary line and includes the description`() {
        val text = EventSharer.buildShareText(event)
        assertTrue(text.startsWith("Mobile Development Meetup \u2014 Tuesday 6:30 PM \u2014 CAS 214"))
        assertTrue(text.endsWith(event.description))
    }

    @Test
    fun `blank parts are skipped so the separator never dangles`() {
        val sparse = event.copy(location = "  ", description = "")
        assertEquals("Mobile Development Meetup \u2014 Tuesday 6:30 PM", EventSharer.buildSummaryLine(sparse))
        assertEquals("Mobile Development Meetup \u2014 Tuesday 6:30 PM", EventSharer.buildShareText(sparse))
    }

    @Test
    fun `send intent is implicit ACTION_SEND with plain text`() {
        val intent = EventSharer.createSendIntent(event)

        assertEquals(Intent.ACTION_SEND, intent.action)
        assertEquals(EventSharer.MIME_TYPE_TEXT, intent.type)
        assertNull("an implicit intent must not name a component", intent.component)
        assertNull("an implicit intent must not name a package", intent.`package`)
        assertEquals(event.title, intent.getStringExtra(Intent.EXTRA_SUBJECT))

        val text = intent.getStringExtra(Intent.EXTRA_TEXT).orEmpty()
        assertTrue(text.contains(event.title))
        assertTrue(text.contains(event.time))
        assertTrue(text.contains(event.location))
        assertFalse(text.isBlank())
    }
}
