package com.priyanshusingh.campuseventexplorer.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.navigation.toRoute
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.CampusEventExplorerApp
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.assertAnyNodeWithTextIsDisplayed
import com.priyanshusingh.campuseventexplorer.ui.onEventItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Verifies the navigation graph with a [TestNavHostController]: start destination, that only
 * the id is placed in the route, back-stack shape, invalid ids and search-state survival across
 * a list -> detail -> list round trip.
 */
@RunWith(AndroidJUnit4::class)
class NavigationFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    private fun launchApp() {
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CampusEventExplorerApp(navController = navController, dynamicColor = false)
        }
    }

    @Test
    fun startDestinationIsEventList() {
        launchApp()

        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        assertTrue(navController.currentDestination?.hasRoute<EventList>() == true)
        assertNull("list is the root; nothing below it", navController.previousBackStackEntry)
    }

    @Test
    fun selectingAnEventNavigatesWithOnlyTheIdInTheRoute() {
        launchApp()
        val target = EventRepository.events[4]

        composeRule.onEventItem(target.id).performClick()
        composeRule.waitForIdle()

        val entry = navController.currentBackStackEntry
        assertNotNull(entry)
        assertTrue(entry!!.destination.hasRoute<EventDetail>())
        assertEquals(target.id, entry.toRoute<EventDetail>().eventId)
        // The event object itself is NOT in the arguments - only the primitive id.
        assertEquals(setOf("eventId"), entry.destination.arguments.keys)

        // The list stays underneath so Back / Up return to it.
        assertTrue(navController.previousBackStackEntry?.destination?.hasRoute<EventList>() == true)
        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()
        composeRule.assertAnyNodeWithTextIsDisplayed(target.title)
    }

    @Test
    fun upButtonPopsBackToTheList() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).performClick()
        composeRule.onNodeWithTag(TestTags.UP_BUTTON).performClick()
        composeRule.waitForIdle()

        assertTrue(navController.currentDestination?.hasRoute<EventList>() == true)
        assertNull(navController.previousBackStackEntry)
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun systemBackPopsBackToTheList() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).performClick()
        composeRule.waitForIdle()

        composeRule.runOnUiThread { navController.popBackStack() }
        composeRule.waitForIdle()

        assertTrue(navController.currentDestination?.hasRoute<EventList>() == true)
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun searchTextSurvivesRoundTripToDetailAndBack() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("tech")
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).performClick()
        composeRule.onNodeWithTag(TestTags.UP_BUTTON).performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TestTags.RESULT_SUMMARY).assertIsDisplayed()
        composeRule.onNodeWithText("Fall Career Fair").assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.eventItem(10L)).assertIsDisplayed()
    }

    @Test
    fun unknownIdShowsEventUnavailableInsteadOfCrashing() {
        launchApp()

        composeRule.runOnUiThread { navController.navigate(EventDetail(eventId = 424242L)) }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE).assertIsDisplayed()
        composeRule.onNodeWithText("Event unavailable").assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SHARE_BUTTON).assertDoesNotExist()
    }

    @Test
    fun negativeAndExtremeIdsAreHandled() {
        launchApp()
        listOf(-1L, 0L, Long.MIN_VALUE, Long.MAX_VALUE).forEach { badId ->
            composeRule.runOnUiThread { navController.navigate(EventDetail(eventId = badId)) }
            composeRule.waitForIdle()
            composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE).assertIsDisplayed()
            composeRule.onNodeWithTag(TestTags.RETURN_TO_EVENTS).performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        }
    }

    @Test
    fun returnToEventsFromUnavailableGoesBackToTheList() {
        launchApp()
        composeRule.runOnUiThread { navController.navigate(EventDetail(eventId = -99L)) }
        composeRule.waitForIdle()

        composeRule.onNodeWithTag(TestTags.RETURN_TO_EVENTS).performClick()
        composeRule.waitForIdle()

        assertTrue(navController.currentDestination?.hasRoute<EventList>() == true)
        assertNull(navController.previousBackStackEntry)
    }

    @Test
    fun rapidDoubleTapOpensOnlyOneDetailEntry() {
        launchApp()
        val item = composeRule.onNodeWithTag(TestTags.eventItem(3L))

        composeRule.mainClock.autoAdvance = false
        item.performClick()
        item.performClick()
        composeRule.mainClock.autoAdvance = true
        composeRule.waitForIdle()

        assertTrue(navController.currentDestination?.hasRoute<EventDetail>() == true)
        // Exactly one detail above the list: popping once lands on the list, not another detail.
        composeRule.runOnUiThread { navController.popBackStack() }
        composeRule.waitForIdle()
        assertTrue(navController.currentDestination?.hasRoute<EventList>() == true)
    }
}
