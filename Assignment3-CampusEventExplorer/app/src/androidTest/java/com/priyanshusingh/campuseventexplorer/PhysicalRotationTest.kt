package com.priyanshusingh.campuseventexplorer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.assertAnyNodeWithTextIsDisplayed
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Device/emulator-only variant of the rotation test that physically rotates the screen with
 * UiAutomator and presses the real hardware Back key. Run with:
 *
 * ```
 * ./gradlew connectedDebugAndroidTest
 * ```
 */
@RunWith(AndroidJUnit4::class)
class PhysicalRotationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @After
    fun restoreOrientation() {
        device.setOrientationNatural()
        device.unfreezeRotation()
    }

    private fun waitForTag(tag: String, timeoutMillis: Long = 30_000) {
        // A software-rendered emulator can stall for several seconds around a rotation
        // (ANR dialogs, GC). Wait generously, and dismiss a system "isn't responding"
        // dialog if one is covering the app.
        composeRule.waitUntil(timeoutMillis = timeoutMillis) {
            dismissAnrDialogIfPresent()
            composeRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /** Taps "Wait" on an ANR dialog so a slow emulator does not hide the app under test. */
    private fun dismissAnrDialogIfPresent() {
        val waitButton = device.findObject(UiSelector().text("Wait"))
        if (waitButton.exists()) {
            waitButton.click()
            device.waitForIdle()
        }
    }

    @Suppress("DEPRECATION") // setOrientationLeft is the most widely supported rotation API.
    private fun rotateToLandscape() {
        device.setOrientationLeft()
        device.waitForIdle(10_000)
        composeRule.waitForIdle()
    }

    private fun pressSystemBack() {
        dismissAnrDialogIfPresent()
        device.pressBack()
        device.waitForIdle(5_000)
        composeRule.waitForIdle()
    }

    @Test
    fun fullAssignmentSequenceWithRealRotation() {
        // 1. Launch
        waitForTag(TestTags.LIST_SCREEN)

        // 2. Search text that reduces the list
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("tech")
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertDoesNotExist()

        // 3. Select an event
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).performClick()
        waitForTag(TestTags.DETAIL_SCREEN)

        // 4. Rotate the emulator
        rotateToLandscape()

        // 5. Detail destination remains visible
        waitForTag(TestTags.DETAIL_SCREEN)
        composeRule.assertAnyNodeWithTextIsDisplayed("Mobile Development Meetup")

        // 6. Press Back
        pressSystemBack()

        // 7. Search text remains
        waitForTag(TestTags.LIST_SCREEN)
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).assertTextContains("tech")
        composeRule.onNodeWithTag(TestTags.eventItem(2L)).assertDoesNotExist()

        // 8. Select another event
        composeRule.onNodeWithTag(TestTags.eventItem(10L)).performClick()
        waitForTag(TestTags.DETAIL_SCREEN)
        composeRule.assertAnyNodeWithTextIsDisplayed("Hack the Charles 24-Hour Hackathon")

        // 9. Press system Back
        pressSystemBack()
        waitForTag(TestTags.LIST_SCREEN)
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).assertTextContains("tech")
    }
}
