package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.DurationBand
import com.priyanshusingh.focusplanbuilder.model.MAX_MINUTES
import com.priyanshusingh.focusplanbuilder.model.MIN_MINUTES
import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Proves the `DurationBand` enum (used by the UI for the scale and tips) can
 * never disagree with the two functions of record.
 */
class DurationBandTest {

    @Test
    fun bandsTileTheValidWindowExactly_withNoGapsOrOverlaps() {
        val covered = DurationBand.entries.flatMap { it.range.toList() }.sorted()
        assertEquals((MIN_MINUTES..MAX_MINUTES).toList(), covered)
    }

    @Test
    fun fromMinutes_agreesWithDurationCategory_forEveryValidMinute() {
        for (minutes in MIN_MINUTES..MAX_MINUTES) {
            val band = DurationBand.fromMinutes(minutes)
            assertEquals("minutes=$minutes", durationCategory(minutes), band?.label)
        }
    }

    @Test
    fun bandBreak_agreesWithRecommendedBreak_forEveryValidMinute() {
        for (minutes in MIN_MINUTES..MAX_MINUTES) {
            val band = DurationBand.fromMinutes(minutes)
            assertEquals("minutes=$minutes", recommendedBreak(minutes), band?.breakMinutes)
        }
    }

    @Test
    fun fromMinutes_isNullOutsideTheWindow() {
        assertNull(DurationBand.fromMinutes(9))
        assertNull(DurationBand.fromMinutes(0))
        assertNull(DurationBand.fromMinutes(-5))
        assertNull(DurationBand.fromMinutes(181))
        assertNull(DurationBand.fromMinutes(Int.MAX_VALUE))
    }

    @Test
    fun fromCategory_roundTripsEveryLabel() {
        for (band in DurationBand.entries) {
            assertEquals(band, DurationBand.fromCategory(band.label))
        }
        assertNull(DurationBand.fromCategory("Invalid"))
        assertNull(DurationBand.fromCategory(""))
        assertNull(DurationBand.fromCategory("Focused Session")) // wrong case
    }

    @Test
    fun shortLabels_areDistinct_andFitOnOneLine() {
        val shortLabels = DurationBand.entries.map { it.shortLabel }
        assertEquals(shortLabels.size, shortLabels.toSet().size)
        assertTrue(shortLabels.all { it.length <= 8 })
    }

    @Test
    fun rangeLabels_readAsExpected() {
        assertEquals("10–29 min", DurationBand.QUICK_REVIEW.rangeLabel)
        assertEquals("30–60 min", DurationBand.FOCUSED_SESSION.rangeLabel)
        assertEquals("61–180 min", DurationBand.EXTENDED_SESSION.rangeLabel)
    }
}
