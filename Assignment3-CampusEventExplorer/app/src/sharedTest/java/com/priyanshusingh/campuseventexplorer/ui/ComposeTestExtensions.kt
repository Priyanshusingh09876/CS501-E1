package com.priyanshusingh.campuseventexplorer.ui

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode

/**
 * Asserts that at least one node with exactly [text] is displayed. Useful when the same string
 * legitimately appears more than once (e.g. an event title in both the app bar and the body).
 */
fun ComposeTestRule.assertAnyNodeWithTextIsDisplayed(text: String) {
    onAllNodesWithText(text).onFirst().assertIsDisplayed()
}

/**
 * Scrolls the event list until the card for [eventId] is composed and returns it, so tests do
 * not depend on how many cards fit on the (device- or Robolectric-sized) screen.
 */
fun ComposeTestRule.onEventItem(eventId: Long): SemanticsNodeInteraction {
    onNodeWithTag(TestTags.EVENT_LIST).performScrollToNode(hasTestTag(TestTags.eventItem(eventId)))
    return onNodeWithTag(TestTags.eventItem(eventId))
}
