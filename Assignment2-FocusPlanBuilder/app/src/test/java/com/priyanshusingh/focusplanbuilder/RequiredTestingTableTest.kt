package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.canCreatePlan
import com.priyanshusingh.focusplanbuilder.model.createFocusPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * The assignment's "Required Testing" table, one parameterised case per row.
 *
 * `canCreatePlan()` mirrors the button rule from FocusPlanRoute exactly
 * (`subject.isNotBlank() && minutes != null && minutes in 10..180`), so a
 * failure here means the button-enabled logic itself is wrong.
 *
 * | Subject | Duration | Expected result                      |
 * |---------|----------|--------------------------------------|
 * | Blank   | 25       | Button disabled                      |
 * | Kotlin  | Blank    | Button disabled                      |
 * | Kotlin  | abc      | Button disabled; no crash            |
 * | Kotlin  | 9        | Button disabled                      |
 * | Kotlin  | 10       | Quick review; 5-minute break         |
 * | Kotlin  | 29       | Quick review; 5-minute break         |
 * | Kotlin  | 30       | Focused session; 10-minute break     |
 * | Kotlin  | 60       | Focused session; 10-minute break     |
 * | Kotlin  | 61       | Extended session; 15-minute break    |
 * | Kotlin  | 180      | Extended session; 15-minute break    |
 * | Kotlin  | 181      | Button disabled                      |
 */
@RunWith(Parameterized::class)
class RequiredTestingTableTest(
    private val subject: String,
    private val minutesText: String,
    private val expectedEnabled: Boolean,
    private val expectedCategory: String?,
    private val expectedBreak: Int?
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: subject=\"{0}\" minutes=\"{1}\" -> enabled={2} {3} {4}")
        fun rows(): List<Array<Any?>> = listOf(
            arrayOf("", "25", false, null, null),
            arrayOf("Kotlin", "", false, null, null),
            arrayOf("Kotlin", "abc", false, null, null),
            arrayOf("Kotlin", "9", false, null, null),
            arrayOf("Kotlin", "10", true, "Quick review", 5),
            arrayOf("Kotlin", "29", true, "Quick review", 5),
            arrayOf("Kotlin", "30", true, "Focused session", 10),
            arrayOf("Kotlin", "60", true, "Focused session", 10),
            arrayOf("Kotlin", "61", true, "Extended session", 15),
            arrayOf("Kotlin", "180", true, "Extended session", 15),
            arrayOf("Kotlin", "181", false, null, null),

            // Extra rows implied by the table: whitespace-only subject, both
            // fields invalid at once, negatives, decimals, and overflow.
            arrayOf("   ", "45", false, null, null),
            arrayOf("", "abc", false, null, null),
            arrayOf("Kotlin", "-10", false, null, null),
            arrayOf("Kotlin", "10.5", false, null, null),
            arrayOf("Kotlin", "99999999999999999999", false, null, null),
            arrayOf("Kotlin", "0", false, null, null)
        )
    }

    @Test
    fun buttonEnabledState_matchesTheTable() {
        // Simply reaching the assertion proves "no crash" for rows like "abc".
        assertEquals(expectedEnabled, canCreatePlan(subject, minutesText))
    }

    @Test
    fun planResult_matchesTheTable() {
        val plan = createFocusPlan(subject, minutesText)

        if (expectedEnabled) {
            assertNotNull(plan)
            assertEquals(expectedCategory, plan!!.category)
            assertEquals(expectedBreak, plan.breakMinutes)
            assertEquals(subject.trim(), plan.subject)
            assertTrue(plan.minutes in 10..180)
        } else {
            assertNull(plan)
            assertFalse(canCreatePlan(subject, minutesText))
        }
    }
}
