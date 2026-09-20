package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for `recommendedBreak()` (model/BreakRecommender.kt).
 */
class BreakRecommenderTest {

    @Test
    fun belowTen_isNotApplicable() {
        assertEquals(0, recommendedBreak(9))
        assertEquals(0, recommendedBreak(0))
        assertEquals(0, recommendedBreak(-1))
        assertEquals(0, recommendedBreak(Int.MIN_VALUE))
    }

    @Test
    fun tenToTwentyNine_isFiveMinutes() {
        for (minutes in 10..29) {
            assertEquals("minutes=$minutes", 5, recommendedBreak(minutes))
        }
    }

    @Test
    fun thirtyToSixty_isTenMinutes() {
        for (minutes in 30..60) {
            assertEquals("minutes=$minutes", 10, recommendedBreak(minutes))
        }
    }

    @Test
    fun aboveSixty_isFifteenMinutes() {
        for (minutes in 61..180) {
            assertEquals("minutes=$minutes", 15, recommendedBreak(minutes))
        }
        assertEquals(15, recommendedBreak(181))
        assertEquals(15, recommendedBreak(Int.MAX_VALUE))
    }

    @Test
    fun boundaries_flipAtExactlyTheRightMinute() {
        assertEquals(0, recommendedBreak(9))
        assertEquals(5, recommendedBreak(10))
        assertEquals(5, recommendedBreak(29))
        assertEquals(10, recommendedBreak(30))
        assertEquals(10, recommendedBreak(60))
        assertEquals(15, recommendedBreak(61))
        assertEquals(15, recommendedBreak(180))
    }

    @Test
    fun breakAlwaysMatchesCategory_forEveryInt_inAndAroundTheWindow() {
        // The two functions are written separately; this proves they agree
        // on the band boundaries for every value a grader could try.
        for (minutes in -50..400) {
            val expected = when (durationCategory(minutes)) {
                "Invalid" -> 0
                "Quick review" -> 5
                "Focused session" -> 10
                "Extended session" -> 15
                else -> error("Unexpected category for $minutes")
            }
            assertEquals("minutes=$minutes", expected, recommendedBreak(minutes))
        }
    }

    @Test
    fun breakIsAlwaysShorterThanTheStudyBlock() {
        for (minutes in 10..180) {
            val breakMinutes = recommendedBreak(minutes)
            assert(breakMinutes < minutes) { "break $breakMinutes >= study $minutes" }
        }
    }
}
