package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.band
import com.priyanshusingh.focusplanbuilder.model.canCreatePlan
import com.priyanshusingh.focusplanbuilder.model.createFocusPlan
import com.priyanshusingh.focusplanbuilder.model.DurationBand
import com.priyanshusingh.focusplanbuilder.model.studyFraction
import com.priyanshusingh.focusplanbuilder.model.totalMinutes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for `createFocusPlan()` / `canCreatePlan()` (model/FocusPlanFactory.kt)
 * and the FocusPlan data class plus its extension properties (model/FocusPlan.kt).
 */
class FocusPlanFactoryTest {

    // ------------------------------------------------------- createFocusPlan

    @Test
    fun buildsTheAssignmentExamplePlan() {
        val plan = createFocusPlan(subject = "Compose State", minutesText = "45")
        assertEquals(
            FocusPlan(subject = "Compose State", minutes = 45, category = "Focused session", breakMinutes = 10),
            plan
        )
    }

    @Test
    fun cleansTheSubject() {
        val plan = createFocusPlan(subject = "   Compose   state  ", minutesText = "30")
        assertEquals("Compose state", plan?.subject)
    }

    @Test
    fun usesBothCalculationFunctions() {
        assertEquals(FocusPlan("Kotlin", 10, "Quick review", 5), createFocusPlan("Kotlin", "10"))
        assertEquals(FocusPlan("Kotlin", 29, "Quick review", 5), createFocusPlan("Kotlin", "29"))
        assertEquals(FocusPlan("Kotlin", 30, "Focused session", 10), createFocusPlan("Kotlin", "30"))
        assertEquals(FocusPlan("Kotlin", 60, "Focused session", 10), createFocusPlan("Kotlin", "60"))
        assertEquals(FocusPlan("Kotlin", 61, "Extended session", 15), createFocusPlan("Kotlin", "61"))
        assertEquals(FocusPlan("Kotlin", 180, "Extended session", 15), createFocusPlan("Kotlin", "180"))
    }

    @Test
    fun returnsNull_forBlankSubject() {
        assertNull(createFocusPlan("", "45"))
        assertNull(createFocusPlan("   ", "45"))
        assertNull(createFocusPlan("\t", "45"))
    }

    @Test
    fun returnsNull_forInvalidMinutes_andNeverThrows() {
        assertNull(createFocusPlan("Kotlin", ""))
        assertNull(createFocusPlan("Kotlin", "abc"))
        assertNull(createFocusPlan("Kotlin", "9"))
        assertNull(createFocusPlan("Kotlin", "181"))
        assertNull(createFocusPlan("Kotlin", "10.5"))
        assertNull(createFocusPlan("Kotlin", "-45"))
        assertNull(createFocusPlan("Kotlin", "99999999999999999999"))
    }

    @Test
    fun returnsNull_whenBothInputsAreInvalid() {
        assertNull(createFocusPlan("", ""))
        assertNull(createFocusPlan("   ", "abc"))
    }

    @Test
    fun neverProducesAnInvalidCategoryOrBreak() {
        for (minutes in 10..180) {
            val plan = createFocusPlan("Kotlin", minutes.toString())
            assertNotNull("minutes=$minutes", plan)
            assertNotEquals("Invalid", plan!!.category)
            assertTrue(plan.breakMinutes in setOf(5, 10, 15))
            assertEquals(minutes, plan.minutes)
        }
    }

    // -------------------------------------------------------- canCreatePlan

    @Test
    fun canCreatePlan_isTrueOnlyWhenBothInputsAreValid() {
        assertTrue(canCreatePlan("Kotlin", "45"))
        assertTrue(canCreatePlan("Kotlin", "10"))
        assertTrue(canCreatePlan("Kotlin", "180"))

        assertFalse(canCreatePlan("", "45"))
        assertFalse(canCreatePlan("   ", "45"))
        assertFalse(canCreatePlan("Kotlin", ""))
        assertFalse(canCreatePlan("Kotlin", "abc"))
        assertFalse(canCreatePlan("Kotlin", "9"))
        assertFalse(canCreatePlan("Kotlin", "181"))
    }

    @Test
    fun canCreatePlan_agreesWithCreateFocusPlan_forManyInputs() {
        val subjects = listOf("", " ", "Kotlin", "  Databases ")
        val minutes = listOf("", " ", "abc", "-1", "0", "9", "10", "29", "30", "60", "61", "180", "181", "10.5", "+45")
        for (subject in subjects) {
            for (text in minutes) {
                val expected = createFocusPlan(subject, text) != null
                assertEquals("subject='$subject' minutes='$text'", expected, canCreatePlan(subject, text))
            }
        }
    }

    // ------------------------------------------------------ FocusPlan class

    @Test
    fun equalPlans_areEqual() {
        val a = FocusPlan("Kotlin", 45, "Focused session", 10)
        val b = FocusPlan("Kotlin", 45, "Focused session", 10)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun plansDifferingByAnyField_areNotEqual() {
        val base = FocusPlan("Kotlin", 45, "Focused session", 10)
        assertNotEquals(base, base.copy(subject = "Databases"))
        assertNotEquals(base, base.copy(minutes = 46))
        assertNotEquals(base, base.copy(category = "Extended session"))
        assertNotEquals(base, base.copy(breakMinutes = 15))
    }

    @Test
    fun totalMinutes_isStudyPlusBreak() {
        assertEquals(55, FocusPlan("Kotlin", 45, "Focused session", 10).totalMinutes)
        assertEquals(15, FocusPlan("Kotlin", 10, "Quick review", 5).totalMinutes)
        assertEquals(195, FocusPlan("Kotlin", 180, "Extended session", 15).totalMinutes)
    }

    @Test
    fun studyFraction_isBetweenZeroAndOne() {
        val plan = FocusPlan("Kotlin", 45, "Focused session", 10)
        assertEquals(45f / 55f, plan.studyFraction, 0.0001f)
        assertEquals(0f, FocusPlan("x", 0, "Invalid", 0).studyFraction, 0f)
    }

    @Test
    fun band_resolvesFromTheCategoryLabel() {
        assertEquals(DurationBand.QUICK_REVIEW, FocusPlan("k", 15, "Quick review", 5).band)
        assertEquals(DurationBand.FOCUSED_SESSION, FocusPlan("k", 45, "Focused session", 10).band)
        assertEquals(DurationBand.EXTENDED_SESSION, FocusPlan("k", 90, "Extended session", 15).band)
        assertNull(FocusPlan("k", 5, "Invalid", 0).band)
    }
}
