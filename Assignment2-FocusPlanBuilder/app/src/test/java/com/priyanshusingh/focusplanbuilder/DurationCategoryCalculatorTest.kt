package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.durationCategory
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for durationCategory() (model/DurationCategoryCalculator.kt).
 * Run via: ./gradlew testDebugUnitTest
 */
class DurationCategoryCalculatorTest {

    @Test
    fun belowTen_isInvalid() {
        assertEquals("Invalid", durationCategory(9))
        assertEquals("Invalid", durationCategory(5))
        assertEquals("Invalid", durationCategory(1))
        assertEquals("Invalid", durationCategory(0))
        assertEquals("Invalid", durationCategory(-1))
        assertEquals("Invalid", durationCategory(-100))
        assertEquals("Invalid", durationCategory(Int.MIN_VALUE))
    }

    @Test
    fun tenToTwentyNine_isQuickReview() {
        assertEquals("Quick review", durationCategory(10)) // lower boundary
        assertEquals("Quick review", durationCategory(15))
        assertEquals("Quick review", durationCategory(20))
        assertEquals("Quick review", durationCategory(29)) // upper boundary
    }

    @Test
    fun thirtyToSixty_isFocusedSession() {
        assertEquals("Focused session", durationCategory(30)) // lower boundary
        assertEquals("Focused session", durationCategory(45))
        assertEquals("Focused session", durationCategory(60)) // upper boundary
    }

    @Test
    fun aboveSixty_isExtendedSession() {
        assertEquals("Extended session", durationCategory(61)) // lower boundary
        assertEquals("Extended session", durationCategory(90))
        assertEquals("Extended session", durationCategory(120))
        assertEquals("Extended session", durationCategory(180))
        assertEquals("Extended session", durationCategory(181)) // outside UI's max
        assertEquals("Extended session", durationCategory(10_000))
        assertEquals("Extended session", durationCategory(Int.MAX_VALUE))
    }

    @Test
    fun exactBoundaryTransitions_flipCategoryAtTheRightPoint() {
        // 29 -> 30 flips Quick review -> Focused session
        assertEquals("Quick review", durationCategory(29))
        assertEquals("Focused session", durationCategory(30))

        // 60 -> 61 flips Focused session -> Extended session
        assertEquals("Focused session", durationCategory(60))
        assertEquals("Extended session", durationCategory(61))

        // 9 -> 10 flips Invalid -> Quick review
        assertEquals("Invalid", durationCategory(9))
        assertEquals("Quick review", durationCategory(10))
    }
}
