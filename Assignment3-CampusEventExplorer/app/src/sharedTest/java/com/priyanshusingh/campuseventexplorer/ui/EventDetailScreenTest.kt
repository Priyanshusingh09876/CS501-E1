package com.priyanshusingh.campuseventexplorer.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.ui.detail.EventDetailScreen
import com.priyanshusingh.campuseventexplorer.ui.theme.CampusEventExplorerTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Contract tests for the stateless [EventDetailScreen]. */
@RunWith(AndroidJUnit4::class)
class EventDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun setScreen(
        event: CampusEvent?,
        onBack: () -> Unit = {},
        onShare: () -> Unit = {},
    ) {
        composeRule.setContent {
            CampusEventExplorerTheme(dynamicColor = false) {
                EventDetailScreen(event = event, onBack = onBack, onShare = onShare)
            }
        }
    }

    @Test
    fun showsEveryRequiredField() {
        val event = EventRepository.events.first()
        setScreen(event)

        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()
        // Title appears both in the app bar and the body; at least one must be visible.
        composeRule.assertAnyNodeWithTextIsDisplayed(event.title)
        composeRule.onNodeWithText(event.category).assertIsDisplayed()
        composeRule.onNodeWithText(event.time).assertIsDisplayed()
        composeRule.onNodeWithText(event.location).assertIsDisplayed()
        composeRule.onNodeWithText(event.description).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SHARE_BUTTON).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun upButtonInvokesOnBack() {
        var backCount = 0
        setScreen(EventRepository.events.first(), onBack = { backCount++ })

        composeRule.onNodeWithContentDescription("Navigate up").performClick()

        assertEquals(1, backCount)
    }

    @Test
    fun shareButtonInvokesOnShare() {
        var shareCount = 0
        setScreen(EventRepository.events.first(), onShare = { shareCount++ })

        composeRule.onNodeWithTag(TestTags.SHARE_BUTTON).performScrollTo().performClick()

        assertEquals(1, shareCount)
    }

    @Test
    fun nullEventShowsUnavailableStateWithoutShareButton() {
        setScreen(event = null)

        composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE).assertIsDisplayed()
        composeRule.onNodeWithText("Event unavailable").assertIsDisplayed()
        composeRule.onNodeWithText("Return to events").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SHARE_BUTTON).assertDoesNotExist()
    }

    @Test
    fun returnToEventsInvokesOnBack() {
        var backCount = 0
        setScreen(event = null, onBack = { backCount++ })

        composeRule.onNodeWithTag(TestTags.RETURN_TO_EVENTS).performClick()

        assertEquals(1, backCount)
    }

    @Test
    fun upButtonStillWorksOnUnavailableScreen() {
        var backCount = 0
        setScreen(event = null, onBack = { backCount++ })

        composeRule.onNodeWithTag(TestTags.UP_BUTTON).performClick()

        assertEquals(1, backCount)
    }

    @Test
    fun veryLongContentDoesNotCrashAndStaysScrollable() {
        val huge = EventRepository.events.first().copy(
            title = "T".repeat(300),
            description = "Lorem ipsum ".repeat(400),
        )
        setScreen(huge)

        composeRule.onNodeWithTag(TestTags.SHARE_BUTTON).performScrollTo().assertIsDisplayed()
    }
}
