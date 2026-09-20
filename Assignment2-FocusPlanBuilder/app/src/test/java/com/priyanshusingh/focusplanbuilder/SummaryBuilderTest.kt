package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.breakLine
import com.priyanshusingh.focusplanbuilder.model.buildSummary
import com.priyanshusingh.focusplanbuilder.model.categoryLine
import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.durationLine
import com.priyanshusingh.focusplanbuilder.model.minutesPreview
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Tests for every user-visible string built in model/SummaryBuilder.kt.
 */
class SummaryBuilderTest {

    private val composeState = FocusPlan(
        subject = "Compose State",
        minutes = 45,
        category = "Focused session",
        breakMinutes = 10
    )

    @Test
    fun summary_matchesTheAssignmentExampleExactly() {
        assertEquals(
            "Study Compose State for 45 minutes, and then take a 10-minute break.",
            buildSummary(composeState)
        )
    }

    @Test
    fun summary_forQuickReview() {
        val plan = FocusPlan("Kotlin", 20, durationCategory(20), recommendedBreak(20))
        assertEquals("Study Kotlin for 20 minutes, and then take a 5-minute break.", buildSummary(plan))
    }

    @Test
    fun summary_forExtendedSession() {
        val plan = FocusPlan("Databases", 90, durationCategory(90), recommendedBreak(90))
        assertEquals("Study Databases for 90 minutes, and then take a 15-minute break.", buildSummary(plan))
    }

    @Test
    fun summary_atBothBoundaries() {
        val at10 = FocusPlan("Kotlin", 10, durationCategory(10), recommendedBreak(10))
        assertEquals("Study Kotlin for 10 minutes, and then take a 5-minute break.", buildSummary(at10))

        val at180 = FocusPlan("Kotlin", 180, durationCategory(180), recommendedBreak(180))
        assertEquals("Study Kotlin for 180 minutes, and then take a 15-minute break.", buildSummary(at180))
    }

    @Test
    fun summary_keepsMultiWordSubjectsVerbatim() {
        val plan = FocusPlan("Compose state", 30, durationCategory(30), recommendedBreak(30))
        assertEquals("Study Compose state for 30 minutes, and then take a 10-minute break.", buildSummary(plan))
    }

    @Test
    fun detailLines_matchTheAssignmentCardExample() {
        assertEquals("Duration: 45 minutes", durationLine(composeState))
        assertEquals("Category: Focused session", categoryLine(composeState))
        assertEquals("Recommended break: 10 minutes", breakLine(composeState))
    }

    @Test
    fun minutesPreview_showsCategoryAndBreak_forValidMinutes() {
        assertEquals("10 min → Quick review · 5-minute break", minutesPreview(10))
        assertEquals("45 min → Focused session · 10-minute break", minutesPreview(45))
        assertEquals("180 min → Extended session · 15-minute break", minutesPreview(180))
    }

    @Test
    fun minutesPreview_isNull_whenMinutesAreMissingOrOutOfRange() {
        assertNull(minutesPreview(null))
        assertNull(minutesPreview(9))
        assertNull(minutesPreview(181))
        assertNull(minutesPreview(-1))
    }
}
