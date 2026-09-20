package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.buildSummary
import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Tests for buildSummary() (model/SummaryBuilder.kt) and for FocusPlan's
 * own generated data-class behavior (model/FocusPlan.kt).
 */
class SummaryBuilderTest {

    @Test
    fun matchesExpectedSentenceFormat_fromAssignmentExample() {
        val plan = FocusPlan(
            subject = "Compose State",
            minutes = 45,
            category = "Focused session",
            breakMinutes = 10
        )
        assertEquals(
            "Study Compose State for 45 minutes, and then take a 10-minute break.",
            buildSummary(plan)
        )
    }

    @Test
    fun worksForQuickReviewCategory() {
        val plan = FocusPlan("Kotlin", 20, durationCategory(20), recommendedBreak(20))
        assertEquals(
            "Study Kotlin for 20 minutes, and then take a 5-minute break.",
            buildSummary(plan)
        )
    }

    @Test
    fun worksForExtendedSessionCategory() {
        val plan = FocusPlan("Databases", 90, durationCategory(90), recommendedBreak(90))
        assertEquals(
            "Study Databases for 90 minutes, and then take a 15-minute break.",
            buildSummary(plan)
        )
    }

    @Test
    fun worksAtExactBoundaryMinutes() {
        val at10 = FocusPlan("Kotlin", 10, durationCategory(10), recommendedBreak(10))
        assertEquals(
            "Study Kotlin for 10 minutes, and then take a 5-minute break.",
            buildSummary(at10)
        )

        val at180 = FocusPlan("Kotlin", 180, durationCategory(180), recommendedBreak(180))
        assertEquals(
            "Study Kotlin for 180 minutes, and then take a 15-minute break.",
            buildSummary(at180)
        )
    }

    @Test
    fun includesSubjectVerbatim_includingMultiWordSubjects() {
        val plan = FocusPlan("Compose state", 30, durationCategory(30), recommendedBreak(30))
        assertEquals(
            "Study Compose state for 30 minutes, and then take a 10-minute break.",
            buildSummary(plan)
        )
    }

    // ---------- FocusPlan data class equality/identity ----------

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
}
