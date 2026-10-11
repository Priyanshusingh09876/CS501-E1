package com.priyanshusingh.campuseventexplorer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.assertAnyNodeWithTextIsDisplayed
import com.priyanshusingh.campuseventexplorer.ui.onEventItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The assignment's "Rotation and state test", end to end, against the real [MainActivity].
 *
 * A rotation is a configuration change that destroys and recreates the Activity. `recreate()`
 * triggers exactly that lifecycle (onSaveInstanceState -> onDestroy -> onCreate -> restore), so
 * the test is deterministic on both Robolectric and a device. A physical-rotation variant lives
 * in `androidTest` ([PhysicalRotationTest]).
 *
 * Sequence under test:
 *  1. Launch the application.
 *  2. Enter search text that reduces the list.
 *  3. Select an event.
 *  4. Rotate (recreate).
 *  5. Verify the detail destination is still visible.
 *  6. Press Back.
 *  7. Verify the search text remains.
 *  8. Select another event.
 *  9. Press system Back.
 */
@RunWith(AndroidJUnit4::class)
class RotationStateTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun rotate() {
        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()
    }

    private fun pressSystemBack() {
        composeRule.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        composeRule.waitForIdle()
    }

    @Test
    fun searchTextAndDestinationSurviveRotation() {
        // 1. Launch - list is the start destination.
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertIsDisplayed()

        // 2. Search text that reduces the list ("tech" -> ids 1 and 10 only).
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("tech")
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertIsDisplayed()

        // 3. Select an event.
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).performClick()
        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()

        // 4. Rotate.
        rotate()

        // 5. Detail destination remains visible with the same event.
        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()
        composeRule.assertAnyNodeWithTextIsDisplayed("Mobile Development Meetup")
        composeRule.onNodeWithText("CAS 214").assertIsDisplayed()

        // 6. Press Back.
        pressSystemBack()

        // 7. Search text remains and the list is still filtered.
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).assertTextContains("tech")
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.eventItem(10L)).assertIsDisplayed()

        // 8. Select another event.
        composeRule.onNodeWithTag(TestTags.eventItem(10L)).performClick()
        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()
        composeRule.assertAnyNodeWithTextIsDisplayed("Hack the Charles 24-Hour Hackathon")

        // 9. Press system Back - back on the (still filtered) list.
        pressSystemBack()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).assertTextContains("tech")
    }

    @Test
    fun rotatingOnTheListKeepsTheSearchText() {
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("career")
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertDoesNotExist()

        rotate()

        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).assertTextContains("career")
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertIsDisplayed()
    }

    @Test
    fun rotatingTwiceOnTheDetailScreenIsStable() {
        composeRule.onEventItem(7L).performClick()

        rotate()
        rotate()

        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()
        composeRule.assertAnyNodeWithTextIsDisplayed("Yoga on the Esplanade")

        pressSystemBack()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }
}
