package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.CATEGORY_EXTENDED_SESSION
import com.priyanshusingh.focusplanbuilder.model.CATEGORY_FOCUSED_SESSION
import com.priyanshusingh.focusplanbuilder.model.CATEGORY_INVALID
import com.priyanshusingh.focusplanbuilder.model.CATEGORY_QUICK_REVIEW
import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.isPlanCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for `durationCategory()` (model/DurationCategory.kt).
 * Run with: ./gradlew testDebugUnitTest
 */
class DurationCategoryTest {

    @Test
    fun assignmentExamples() {
        assertEquals("Quick review", durationCategory(20))
        assertEquals("Focused session", durationCategory(45))
        assertEquals("Extended session", durationCategory(90))
    }

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
        for (minutes in 10..29) {
            assertEquals("minutes=$minutes", "Quick review", durationCategory(minutes))
        }
    }

    @Test
    fun thirtyToSixty_isFocusedSession() {
        for (minutes in 30..60) {
            assertEquals("minutes=$minutes", "Focused session", durationCategory(minutes))
        }
    }

    @Test
    fun sixtyOneAndAbove_isExtendedSession() {
        for (minutes in 61..180) {
            assertEquals("minutes=$minutes", "Extended session", durationCategory(minutes))
        }
        assertEquals("Extended session", durationCategory(181))
        assertEquals("Extended session", durationCategory(10_000))
        assertEquals("Extended session", durationCategory(Int.MAX_VALUE))
    }

    @Test
    fun boundaries_flipAtExactlyTheRightMinute() {
        assertEquals("Invalid", durationCategory(9))
        assertEquals("Quick review", durationCategory(10))

        assertEquals("Quick review", durationCategory(29))
        assertEquals("Focused session", durationCategory(30))

        assertEquals("Focused session", durationCategory(60))
        assertEquals("Extended session", durationCategory(61))
    }

    @Test
    fun labelsMatchTheConstants() {
        assertEquals(CATEGORY_INVALID, durationCategory(0))
        assertEquals(CATEGORY_QUICK_REVIEW, durationCategory(15))
        assertEquals(CATEGORY_FOCUSED_SESSION, durationCategory(45))
        assertEquals(CATEGORY_EXTENDED_SESSION, durationCategory(120))
    }

    @Test
    fun everyIntInTheValidWindow_getsARealCategory() {
        for (minutes in 10..180) {
            assertTrue("minutes=$minutes", isPlanCategory(durationCategory(minutes)))
        }
    }

    @Test
    fun isPlanCategory_rejectsInvalidAndUnknownLabels() {
        assertFalse(isPlanCategory("Invalid"))
        assertFalse(isPlanCategory(""))
        assertFalse(isPlanCategory("quick review")) // case-sensitive on purpose
        assertTrue(isPlanCategory("Quick review"))
        assertTrue(isPlanCategory("Focused session"))
        assertTrue(isPlanCategory("Extended session"))
    }
}
