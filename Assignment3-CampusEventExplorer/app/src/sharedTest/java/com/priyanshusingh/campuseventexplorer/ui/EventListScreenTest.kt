package com.priyanshusingh.campuseventexplorer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.data.filterByQuery
import com.priyanshusingh.campuseventexplorer.ui.list.EventListScreen
import com.priyanshusingh.campuseventexplorer.ui.theme.CampusEventExplorerTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Contract tests for the stateless [EventListScreen]. */
@RunWith(AndroidJUnit4::class)
class EventListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val events = EventRepository.events

    /** Hosts the screen the same way the real route does: query hoisted, list derived. */
    private fun setScreen(
        initialQuery: String = "",
        onEventSelected: (Long) -> Unit = {},
    ) {
        composeRule.setContent {
            var query by remember { mutableStateOf(initialQuery) }
            CampusEventExplorerTheme(dynamicColor = false) {
                EventListScreen(
                    events = events.filterByQuery(query),
                    query = query,
                    onQueryChange = { query = it },
                    onEventSelected = onEventSelected,
                    totalEventCount = events.size,
                    categories = EventRepository.categories,
                )
            }
        }
    }

    @Test
    fun listItemShowsTitleCategoryLocationAndTime() {
        setScreen()
        val first = events.first()

        composeRule.onNodeWithTag(TestTags.eventItem(first.id)).assertIsDisplayed()
        composeRule.onNodeWithText(first.title).assertIsDisplayed()
        composeRule.onNodeWithText(first.location).assertIsDisplayed()
        composeRule.onNodeWithText(first.time).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.eventItem(first.id)).assertTextContains(first.category)
    }

    @Test
    fun typingFiltersTheList() {
        setScreen()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("career")

        composeRule.onNodeWithText("Fall Career Fair").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.RESULT_SUMMARY).assertTextContains("2 of 12", substring = true)
    }

    @Test
    fun noMatchShowsEmptyStateAndClearRestoresList() {
        setScreen()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("zzzz-no-such-event")

        composeRule.onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.EVENT_LIST).assertDoesNotExist()

        composeRule.onNodeWithText("Clear search").performClick()

        composeRule.onNodeWithTag(TestTags.EMPTY_STATE).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertIsDisplayed()
    }

    @Test
    fun clearIconClearsTheQuery() {
        setScreen(initialQuery = "tech")
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertDoesNotExist()

        composeRule.onNodeWithTag(TestTags.CLEAR_SEARCH).performClick()

        composeRule.onNodeWithTag(TestTags.CLEAR_SEARCH).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.RESULT_SUMMARY).assertTextContains("12 events")
    }

    @Test
    fun categoryChipSetsAndTogglesTheQuery() {
        setScreen()
        val chip = composeRule.onNodeWithTag(TestTags.categoryChip("Career"))

        chip.performClick()
        chip.assertIsSelected()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).assertTextContains("Career")
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertDoesNotExist()

        chip.performClick()
        chip.assertIsNotSelected()
        composeRule.onNodeWithTag(TestTags.RESULT_SUMMARY).assertTextContains("12 events")
    }

    @Test
    fun whitespaceOnlyQueryShowsEveryEvent() {
        setScreen()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("   ")
        composeRule.onNodeWithTag(TestTags.RESULT_SUMMARY).assertTextContains("12 events")
    }

    @Test
    fun selectingAnEventReportsOnlyItsId() {
        val selected = mutableListOf<Long>()
        setScreen(onEventSelected = { selected += it })

        composeRule.onNodeWithTag(TestTags.eventItem(3L)).performClick()

        assertEquals(listOf(3L), selected)
    }

    @Test
    fun searchFieldTextIsControlledByCaller() {
        setScreen(initialQuery = "yoga")
        composeRule.onNodeWithText("Yoga on the Esplanade").assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextClearance()
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertIsDisplayed()
    }

    @Test
    fun emptyCollectionWithoutQueryRendersWithoutCrash() {
        composeRule.setContent {
            CampusEventExplorerTheme(dynamicColor = false) {
                EventListScreen(
                    events = emptyList<CampusEvent>(),
                    query = "",
                    onQueryChange = {},
                    onEventSelected = {},
                )
            }
        }
        composeRule.onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()
    }
}
