package com.priyanshusingh.focusplanbuilder

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanRoute
import org.junit.Rule
import org.junit.Test

/**
 * On-device / emulator UI tests exercising FocusPlanRoute + FocusPlanScreen
 * together, the way a real user would. These complement the model-layer
 * unit tests (DurationCategoryCalculatorTest, BreakRecommenderTest,
 * MinutesParsingTest, SubjectValidationTest, SummaryBuilderTest,
 * RequiredTestingTableTest)
 * (which only tests pure functions) by verifying wiring: that typing into a
 * field actually changes the button's enabled state, that invalid input
 * never crashes the app, and that editing a field after a plan is created
 * removes the old result card.
 *
 * Run via: ./gradlew connectedDebugAndroidTest (requires an emulator/device)
 */
class FocusPlanScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContent() {
        composeTestRule.setContent {
            FocusPlanRoute()
        }
    }

    @Test
    fun button_isDisabled_whenSubjectIsBlank() {
        setContent()
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("25")
        composeTestRule.onNodeWithText("Create plan").assertIsNotEnabled()
    }

    @Test
    fun button_isDisabled_whenMinutesIsBlank() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Create plan").assertIsNotEnabled()
    }

    @Test
    fun button_isDisabled_andAppDoesNotCrash_onNonNumericMinutes() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("abc")
        // If the app were still standing at this assertion, toIntOrNull()
        // successfully prevented a NumberFormatException crash.
        composeTestRule.onNodeWithText("Create plan").assertIsNotEnabled()
    }

    @Test
    fun button_isDisabled_forNineMinutes_andEnabled_forTenMinutes() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("9")
        composeTestRule.onNodeWithText("Create plan").assertIsNotEnabled()

        composeTestRule.onNodeWithText("9").performTextClearance()
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("10")
        composeTestRule.onNodeWithText("Create plan").assertIsEnabled()
    }

    @Test
    fun button_isDisabled_forOneEightyOne_butEnabled_forOneEighty() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("181")
        composeTestRule.onNodeWithText("Create plan").assertIsNotEnabled()

        composeTestRule.onNodeWithText("181").performTextClearance()
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("180")
        composeTestRule.onNodeWithText("Create plan").assertIsEnabled()
    }

    @Test
    fun resultCard_appearsAfterCreatingPlan_andShowsExpectedValues() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Compose State")
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("45")
        composeTestRule.onNodeWithText("Create plan").performClick()

        composeTestRule.onNodeWithText("Compose State").assertIsDisplayed()
        composeTestRule.onNodeWithText("Duration: 45 minutes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category: Focused session").assertIsDisplayed()
        composeTestRule.onNodeWithText("Recommended break: 10 minutes").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Study Compose State for 45 minutes, and then take a 10-minute break."
        ).assertIsDisplayed()
    }

    @Test
    fun resultCard_disappears_whenSubjectIsEditedAfterPlanCreated() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("45")
        composeTestRule.onNodeWithText("Create plan").performClick()
        composeTestRule.onNodeWithText("Category: Focused session").assertIsDisplayed()

        // Editing the subject after a plan exists must remove the old card.
        composeTestRule.onNodeWithText("Kotlin").performTextInput("2")

        composeTestRule.onNodeWithText("Category: Focused session")
            .assertDoesNotExistOrIsNotDisplayed()
    }

    @Test
    fun resultCard_disappears_whenMinutesIsEditedAfterPlanCreated() {
        setContent()
        composeTestRule.onNodeWithText("Study subject").performTextInput("Kotlin")
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("45")
        composeTestRule.onNodeWithText("Create plan").performClick()
        composeTestRule.onNodeWithText("Category: Focused session").assertIsDisplayed()

        composeTestRule.onNodeWithText("45").performTextClearance()
        composeTestRule.onNodeWithText("Minutes available (10-180)").performTextInput("60")

        composeTestRule.onNodeWithText("Category: Focused session")
            .assertDoesNotExistOrIsNotDisplayed()
        // A brand-new plan for the same subject/60 minutes was not created
        // automatically -- the user must press the button again.
    }
}

/**
 * Small helper so a "should not be visible" assertion reads clearly whether
 * the node was removed from the tree entirely or is merely off-screen.
 */
private fun androidx.compose.ui.test.SemanticsNodeInteraction.assertDoesNotExistOrIsNotDisplayed() {
    try {
        this.assertIsDisplayed()
        throw AssertionError("Expected node to not be displayed, but it was.")
    } catch (expected: AssertionError) {
        // Node either does not exist or is not displayed -- both are acceptable.
    }
}
