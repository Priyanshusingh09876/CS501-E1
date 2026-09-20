package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.isSubjectValid
import com.priyanshusingh.focusplanbuilder.model.parseValidMinutes
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Reproduces, row by row, the "Required Testing" table from the assignment
 * PDF. This mirrors exactly how FocusPlanRoute computes canCreatePlan, so a
 * failure here means the button-enabled logic itself is wrong, not just a
 * calculation function.
 */
class RequiredTestingTableTest {

    /** Mirrors FocusPlanRoute's canCreatePlan calculation exactly. */
    private fun canCreatePlan(subject: String, minutesText: String): Boolean =
        isSubjectValid(subject) && parseValidMinutes(minutesText) != null

    @Test
    fun row_blankSubject_25minutes_buttonDisabled() {
        assertFalse(canCreatePlan(subject = "", minutesText = "25"))
    }

    @Test
    fun row_kotlin_blankMinutes_buttonDisabled() {
        assertFalse(canCreatePlan(subject = "Kotlin", minutesText = ""))
    }

    @Test
    fun row_kotlin_abc_buttonDisabled_noCrash() {
        // The mere fact that this test method returns instead of throwing
        // demonstrates "no crash" for non-numeric input.
        assertFalse(canCreatePlan(subject = "Kotlin", minutesText = "abc"))
    }

    @Test
    fun row_kotlin_9minutes_buttonDisabled() {
        assertFalse(canCreatePlan(subject = "Kotlin", minutesText = "9"))
    }

    @Test
    fun row_kotlin_10minutes_quickReview_5minuteBreak() {
        assertTrue(canCreatePlan(subject = "Kotlin", minutesText = "10"))
        val minutes = parseValidMinutes("10")!!
        assertTrue(durationCategory(minutes) == "Quick review")
        assertTrue(recommendedBreak(minutes) == 5)
    }

    @Test
    fun row_kotlin_29minutes_quickReview_5minuteBreak() {
        assertTrue(canCreatePlan(subject = "Kotlin", minutesText = "29"))
        val minutes = parseValidMinutes("29")!!
        assertTrue(durationCategory(minutes) == "Quick review")
        assertTrue(recommendedBreak(minutes) == 5)
    }

    @Test
    fun row_kotlin_30minutes_focusedSession_10minuteBreak() {
        assertTrue(canCreatePlan(subject = "Kotlin", minutesText = "30"))
        val minutes = parseValidMinutes("30")!!
        assertTrue(durationCategory(minutes) == "Focused session")
        assertTrue(recommendedBreak(minutes) == 10)
    }

    @Test
    fun row_kotlin_60minutes_focusedSession_10minuteBreak() {
        assertTrue(canCreatePlan(subject = "Kotlin", minutesText = "60"))
        val minutes = parseValidMinutes("60")!!
        assertTrue(durationCategory(minutes) == "Focused session")
        assertTrue(recommendedBreak(minutes) == 10)
    }

    @Test
    fun row_kotlin_61minutes_extendedSession_15minuteBreak() {
        assertTrue(canCreatePlan(subject = "Kotlin", minutesText = "61"))
        val minutes = parseValidMinutes("61")!!
        assertTrue(durationCategory(minutes) == "Extended session")
        assertTrue(recommendedBreak(minutes) == 15)
    }

    @Test
    fun row_kotlin_180minutes_extendedSession_15minuteBreak() {
        assertTrue(canCreatePlan(subject = "Kotlin", minutesText = "180"))
        val minutes = parseValidMinutes("180")!!
        assertTrue(durationCategory(minutes) == "Extended session")
        assertTrue(recommendedBreak(minutes) == 15)
    }

    @Test
    fun row_kotlin_181minutes_buttonDisabled() {
        assertFalse(canCreatePlan(subject = "Kotlin", minutesText = "181"))
    }

    // ---------- Additional cross-checks not in the table, but implied by it ----------

    @Test
    fun whitespaceOnlySubject_behavesLikeBlank() {
        assertFalse(canCreatePlan(subject = "   ", minutesText = "45"))
    }

    @Test
    fun bothFieldsInvalidSimultaneously_stillJustDisabled_noCrash() {
        assertFalse(canCreatePlan(subject = "", minutesText = "abc"))
        assertFalse(canCreatePlan(subject = "   ", minutesText = ""))
    }

    @Test
    fun negativeMinutes_buttonDisabled_noCrash() {
        assertFalse(canCreatePlan(subject = "Kotlin", minutesText = "-10"))
    }

    @Test
    fun hugeMinutesString_buttonDisabled_noCrash() {
        assertFalse(canCreatePlan(subject = "Kotlin", minutesText = "99999999999999999999"))
    }
}
