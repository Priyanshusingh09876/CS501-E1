package com.priyanshusingh.campuseventexplorer.navigation

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.net.toUri
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.MainActivity
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.assertAnyNodeWithTextIsDisplayed
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Exercises the detail destination through the `campusevents://event/{eventId}` deep link, which
 * is the only way a *real* user can hand the app an arbitrary id. Covers valid, unknown and
 * malformed ids - none of them may crash - and both ways a deep link can arrive:
 *
 * - **as a new task** (`adb shell am start`, launcher, most browsers): Navigation synthesises
 *   `EventList` underneath the detail, so Up simply pops;
 * - **inside another task** (no `FLAG_ACTIVITY_NEW_TASK`): only the detail is on the stack, and
 *   the app must still land on the list when the user presses Up / "Return to events".
 */
@RunWith(AndroidJUnit4::class)
class DeepLinkTest {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private var scenario: ActivityScenario<MainActivity>? = null

    @After
    fun tearDown() {
        scenario?.close()
    }

    private fun launch(path: String, newTask: Boolean = true) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "$EVENT_DEEP_LINK_BASE/$path".toUri(),
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java,
        )
        if (newTask) {
            // A launcher / `am start` deep link arrives with NEW_TASK; NavController then relaunches
            // the Activity once more with CLEAR_TASK added so it can build a predictable stack.
            // Launching with both flags is that second, final instance.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        scenario = ActivityScenario.launch(intent)
        composeRule.waitForIdle()
    }

    /**
     * With a new task Navigation first lands on EventList and then *animates* to the deep-linked
     * destination, so wait until the expected screen has fully slid into place.
     */
    private fun waitUntilDisplayed(tag: String) {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            runCatching { composeRule.onNodeWithTag(tag).assertIsDisplayed() }.isSuccess
        }
    }

    @Test
    fun validIdOpensThatEventAndUpReturnsToTheList() {
        launch("3")
        waitUntilDisplayed(TestTags.DETAIL_SCREEN)

        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()
        composeRule.assertAnyNodeWithTextIsDisplayed("Intro to Kotlin Coroutines Workshop")

        composeRule.onNodeWithTag(TestTags.UP_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun validIdFromAnotherTaskStillReturnsToTheListOnUp() {
        launch("3", newTask = false)
        waitUntilDisplayed(TestTags.DETAIL_SCREEN)

        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN).assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.UP_BUTTON).performClick()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).assertIsDisplayed()
    }

    @Test
    fun unknownIdShowsEventUnavailable() {
        launch("999")
        waitUntilDisplayed(TestTags.UNAVAILABLE_STATE)

        composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE).assertIsDisplayed()
        composeRule.onNodeWithText("Event unavailable").assertIsDisplayed()

        composeRule.onNodeWithTag(TestTags.RETURN_TO_EVENTS).performClick()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun unknownIdFromAnotherTaskReturnsToTheList() {
        launch("999", newTask = false)
        waitUntilDisplayed(TestTags.UNAVAILABLE_STATE)

        composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.RETURN_TO_EVENTS).performClick()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun negativeIdShowsEventUnavailable() {
        launch("-5")
        waitUntilDisplayed(TestTags.UNAVAILABLE_STATE)
        composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE).assertIsDisplayed()
    }

    @Test
    fun malformedIdFallsBackToTheListWithoutCrashing() {
        launch("not-a-number")
        waitUntilDisplayed(TestTags.LIST_SCREEN)
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }

    @Test
    fun hugeNumericIdDoesNotCrash() {
        // Overflows Long -> the deep link does not match -> start destination.
        launch("99999999999999999999999")
        waitUntilDisplayed(TestTags.LIST_SCREEN)
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
    }
}
