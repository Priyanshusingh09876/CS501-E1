package com.priyanshusingh.focusplanbuilder

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanRoute
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanTestTags
import org.junit.Rule
import org.junit.Test

/**
 * On-device tests that exercise FocusPlanRoute + FocusPlanScreen together, the
 * way a user would. They cover the wiring the pure unit tests cannot: typing
 * changes the button, invalid input never crashes, editing removes the old
 * card, and rememberSaveable restores the fields after recreation.
 *
 * Nodes are located by test tag (see FocusPlanTestTags) rather than by visible
 * text so the same string appearing in a field and on the card is never
 * ambiguous.
 *
 * The screen is a vertically scrolling Column, so on a phone the result card
 * (and, with the keyboard open, even the Create button) can sit below the fold.
 * `assertIsDisplayed()` fails for a node that exists but is off-screen, and a
 * click aimed below the viewport is dropped, so every helper here scrolls the
 * target into view first with `performScrollTo()`.
 *
 * Run with: ./gradlew connectedDebugAndroidTest  (needs an emulator or device)
 */
class FocusPlanScreenTest {

    @get:Rule
    val rule = createComposeRule()

    // ---------------------------------------------------------- helpers

    private fun ComposeContentTestRule.subject(): SemanticsNodeInteraction =
        onNodeWithTag(FocusPlanTestTags.SUBJECT_FIELD)

    private fun ComposeContentTestRule.minutes(): SemanticsNodeInteraction =
        onNodeWithTag(FocusPlanTestTags.MINUTES_FIELD)

    private fun ComposeContentTestRule.createButton(): SemanticsNodeInteraction =
        onNodeWithTag(FocusPlanTestTags.CREATE_BUTTON)

    private fun ComposeContentTestRule.card(): SemanticsNodeInteraction =
        onNodeWithTag(FocusPlanTestTags.RESULT_CARD)

    /** Scrolls the Create button into view, then taps it. */
    private fun clickCreate() {
        rule.createButton().performScrollTo().performClick()
    }

    /** Scrolls the result card into view and asserts it is actually visible. */
    private fun assertCardDisplayed() {
        rule.card().performScrollTo().assertIsDisplayed()
    }

    /** A card node (subject line, summary, ...) scrolled into view first. */
    private fun cardNode(tag: String): SemanticsNodeInteraction =
        rule.onNodeWithTag(tag).performScrollTo()

    private fun launch() {
        rule.setContent { FocusPlanRoute() }
    }

    private fun enter(subject: String, minutes: String) {
        if (subject.isNotEmpty()) rule.subject().performTextInput(subject)
        if (minutes.isNotEmpty()) rule.minutes().performTextInput(minutes)
    }

    // ------------------------------------------------- required table rows

    @Test
    fun blankSubject_25minutes_buttonDisabled() {
        launch()
        enter("", "25")
        rule.createButton().assertIsNotEnabled()
    }

    @Test
    fun kotlin_blankMinutes_buttonDisabled() {
        launch()
        enter("Kotlin", "")
        rule.createButton().assertIsNotEnabled()
    }

    @Test
    fun kotlin_abc_buttonDisabled_andNoCrash() {
        launch()
        enter("Kotlin", "abc")
        // Reaching this assertion proves toIntOrNull() prevented a crash.
        rule.createButton().assertIsNotEnabled()
        rule.minutes().assertTextContains("Enter whole minutes using digits only.")
    }

    @Test
    fun kotlin_9_buttonDisabled() {
        launch()
        enter("Kotlin", "9")
        rule.createButton().assertIsNotEnabled()
    }

    @Test
    fun kotlin_10_quickReview_5minuteBreak() {
        launch()
        enter("Kotlin", "10")
        rule.createButton().assertIsEnabled()
        clickCreate()
        assertCardShows("Kotlin", 10, "Quick review", 5)
    }

    @Test
    fun kotlin_29_quickReview_5minuteBreak() {
        launch()
        enter("Kotlin", "29")
        clickCreate()
        assertCardShows("Kotlin", 29, "Quick review", 5)
    }

    @Test
    fun kotlin_30_focusedSession_10minuteBreak() {
        launch()
        enter("Kotlin", "30")
        clickCreate()
        assertCardShows("Kotlin", 30, "Focused session", 10)
    }

    @Test
    fun kotlin_60_focusedSession_10minuteBreak() {
        launch()
        enter("Kotlin", "60")
        clickCreate()
        assertCardShows("Kotlin", 60, "Focused session", 10)
    }

    @Test
    fun kotlin_61_extendedSession_15minuteBreak() {
        launch()
        enter("Kotlin", "61")
        clickCreate()
        assertCardShows("Kotlin", 61, "Extended session", 15)
    }

    @Test
    fun kotlin_180_extendedSession_15minuteBreak() {
        launch()
        enter("Kotlin", "180")
        clickCreate()
        assertCardShows("Kotlin", 180, "Extended session", 15)
    }

    @Test
    fun kotlin_181_buttonDisabled() {
        launch()
        enter("Kotlin", "181")
        rule.createButton().assertIsNotEnabled()
        rule.minutes().assertTextContains("Maximum is 180 minutes. 181 is too long.")
    }

    // ------------------------------------------------ additional checks

    @Test
    fun resultCard_isNotVisible_beforeFirstPlan() {
        launch()
        rule.card().assertDoesNotExist()
        enter("Kotlin", "45")
        rule.card().assertDoesNotExist()
    }

    @Test
    fun resultCard_showsAssignmentExample_exactly() {
        launch()
        enter("Compose State", "45")
        clickCreate()

        cardNode(FocusPlanTestTags.RESULT_SUBJECT).assertTextEquals("Compose State")
        cardNode(FocusPlanTestTags.RESULT_DURATION).assertTextEquals("Duration: 45 minutes")
        cardNode(FocusPlanTestTags.RESULT_CATEGORY).assertTextEquals("Category: Focused session")
        cardNode(FocusPlanTestTags.RESULT_BREAK).assertTextEquals("Recommended break: 10 minutes")
        cardNode(FocusPlanTestTags.RESULT_SUMMARY)
            .assertTextEquals("Study Compose State for 45 minutes, and then take a 10-minute break.")
    }

    @Test
    fun subjectIsCleaned_onTheCard() {
        launch()
        enter("   Compose   state  ", "45")
        clickCreate()
        cardNode(FocusPlanTestTags.RESULT_SUBJECT).assertTextEquals("Compose state")
    }

    @Test
    fun erasingMinutes_afterPlan_doesNotCrash_andDisablesButton() {
        launch()
        enter("Kotlin", "45")
        clickCreate()
        assertCardDisplayed()

        rule.minutes().performTextClearance()

        rule.createButton().assertIsNotEnabled()
        rule.card().assertDoesNotExist()
    }

    @Test
    fun editingSubject_afterPlan_removesOldCard() {
        launch()
        enter("Kotlin", "45")
        clickCreate()
        assertCardDisplayed()

        rule.subject().performTextInput(" 2")

        rule.card().assertDoesNotExist()
        // The button is still enabled (input is still valid) but no new plan
        // appears until the user taps it again.
        rule.createButton().assertIsEnabled()
    }

    @Test
    fun editingMinutes_afterPlan_removesOldCard_andNewPlanNeedsAnotherTap() {
        launch()
        enter("Kotlin", "45")
        clickCreate()
        cardNode(FocusPlanTestTags.RESULT_CATEGORY).assertTextEquals("Category: Focused session")

        rule.minutes().performTextReplacement("90")
        rule.card().assertDoesNotExist()

        clickCreate()
        cardNode(FocusPlanTestTags.RESULT_CATEGORY).assertTextEquals("Category: Extended session")
    }

    @Test
    fun buttonEnabledState_followsInputAutomatically() {
        launch()
        rule.createButton().assertIsNotEnabled()

        rule.subject().performTextInput("Kotlin")
        rule.createButton().assertIsNotEnabled()

        rule.minutes().performTextInput("9")
        rule.createButton().assertIsNotEnabled()

        rule.minutes().performTextReplacement("10")
        rule.createButton().assertIsEnabled()

        rule.minutes().performTextReplacement("181")
        rule.createButton().assertIsNotEnabled()

        rule.minutes().performTextReplacement("180")
        rule.createButton().assertIsEnabled()

        rule.subject().performTextClearance()
        rule.createButton().assertIsNotEnabled()
    }

    @Test
    fun quickPickChip_fillsMinutes_andEnablesButton() {
        launch()
        rule.subject().performTextInput("Databases")
        rule.onNodeWithTag("${FocusPlanTestTags.QUICK_PICK_PREFIX}45").performScrollTo().performClick()

        rule.minutes().assertTextContains("45")
        rule.createButton().assertIsEnabled()
        clickCreate()
        assertCardShows("Databases", 45, "Focused session", 10)
    }

    @Test
    fun startOver_clearsEverything() {
        launch()
        enter("Kotlin", "45")
        clickCreate()
        assertCardDisplayed()

        rule.onNodeWithTag(FocusPlanTestTags.START_OVER_BUTTON).performScrollTo().performClick()

        rule.card().assertDoesNotExist()
        rule.createButton().assertIsNotEnabled()
    }

    // -------------------------------------------------- saved state

    @Test
    fun inputs_andPlan_surviveActivityRecreation() {
        val restorationTester = StateRestorationTester(rule)
        restorationTester.setContent { FocusPlanRoute() }

        enter("Kotlin", "45")
        clickCreate()
        assertCardDisplayed()

        // Simulates the save/restore cycle that a rotation triggers.
        restorationTester.emulateSavedInstanceStateRestore()

        rule.subject().assertTextContains("Kotlin")
        rule.minutes().assertTextContains("45")
        rule.createButton().assertIsEnabled()
        cardNode(FocusPlanTestTags.RESULT_SUMMARY)
            .assertTextEquals("Study Kotlin for 45 minutes, and then take a 10-minute break.")
    }

    @Test
    fun invalidInputs_alsoSurviveRecreation_withoutCrashing() {
        val restorationTester = StateRestorationTester(rule)
        restorationTester.setContent { FocusPlanRoute() }

        enter("   ", "abc")
        restorationTester.emulateSavedInstanceStateRestore()

        rule.minutes().assertTextContains("abc")
        rule.createButton().assertIsNotEnabled()
    }

    // ---------------------------------------------------------- assertions

    private fun assertCardShows(subject: String, minutes: Int, category: String, breakMinutes: Int) {
        assertCardDisplayed()
        cardNode(FocusPlanTestTags.RESULT_SUBJECT).assertTextEquals(subject)
        cardNode(FocusPlanTestTags.RESULT_DURATION).assertTextEquals("Duration: $minutes minutes")
        cardNode(FocusPlanTestTags.RESULT_CATEGORY).assertTextEquals("Category: $category")
        cardNode(FocusPlanTestTags.RESULT_BREAK)
            .assertTextEquals("Recommended break: $breakMinutes minutes")
        cardNode(FocusPlanTestTags.RESULT_SUMMARY).assertTextEquals(
            "Study $subject for $minutes minutes, and then take a $breakMinutes-minute break."
        )
    }
}
