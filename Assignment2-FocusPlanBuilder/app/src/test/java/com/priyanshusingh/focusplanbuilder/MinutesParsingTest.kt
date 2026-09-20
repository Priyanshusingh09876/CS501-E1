package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.isNumeric
import com.priyanshusingh.focusplanbuilder.model.isOutOfRange
import com.priyanshusingh.focusplanbuilder.model.parseValidMinutes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for parseValidMinutes(), isNumeric(), and isOutOfRange()
 * (model/MinutesParsing.kt). This is the highest-risk file in the app for
 * crashes, so it gets the heaviest edge-case coverage.
 */
class MinutesParsingTest {

    // ---------- parseValidMinutes(): blank / empty ----------

    @Test
    fun blankOrEmptyText_returnsNull() {
        assertNull(parseValidMinutes(""))
        assertNull(parseValidMinutes(" "))
        assertNull(parseValidMinutes("   "))
    }

    // ---------- parseValidMinutes(): non-numeric ----------

    @Test
    fun nonNumericText_returnsNull_andDoesNotThrow() {
        assertNull(parseValidMinutes("abc"))
        assertNull(parseValidMinutes("Kotlin"))
        assertNull(parseValidMinutes("!!!"))
        assertNull(parseValidMinutes("45abc"))
        assertNull(parseValidMinutes("abc45"))
        assertNull(parseValidMinutes("4a5"))
    }

    @Test
    fun decimalText_returnsNull() {
        // toIntOrNull() does not parse decimals; "10.5" is not truncated
        // to 10, it is rejected outright.
        assertNull(parseValidMinutes("10.5"))
        assertNull(parseValidMinutes("45.0"))
        assertNull(parseValidMinutes(".5"))
    }

    @Test
    fun textWithInternalWhitespace_returnsNull() {
        // toIntOrNull() requires the *entire* string to be a valid integer;
        // surrounding or embedded whitespace is not trimmed automatically.
        assertNull(parseValidMinutes(" 45"))
        assertNull(parseValidMinutes("45 "))
        assertNull(parseValidMinutes("4 5"))
    }

    // ---------- parseValidMinutes(): negative / zero ----------

    @Test
    fun negativeOrZero_returnsNull() {
        assertNull(parseValidMinutes("-1"))
        assertNull(parseValidMinutes("-5"))
        assertNull(parseValidMinutes("-100"))
        assertNull(parseValidMinutes("0"))
    }

    // ---------- parseValidMinutes(): out of range ----------

    @Test
    fun belowMinimum_returnsNull() {
        assertNull(parseValidMinutes("1"))
        assertNull(parseValidMinutes("5"))
        assertNull(parseValidMinutes("9"))
    }

    @Test
    fun aboveMaximum_returnsNull() {
        assertNull(parseValidMinutes("181"))
        assertNull(parseValidMinutes("200"))
        assertNull(parseValidMinutes("999999"))
    }

    // ---------- parseValidMinutes(): overflow ----------

    @Test
    fun numberTooLargeForInt_returnsNull_andDoesNotThrow() {
        // A number bigger than Int.MAX_VALUE makes toIntOrNull() return
        // null rather than overflowing or throwing -- this is exactly the
        // safety toIntOrNull() is meant to provide over toInt().
        assertNull(parseValidMinutes("99999999999999999999"))
        assertNull(parseValidMinutes("2147483648")) // Int.MAX_VALUE + 1
    }

    // ---------- parseValidMinutes(): valid boundary and mid-range values ----------

    @Test
    fun exactBoundaries_areValid() {
        assertEquals(10, parseValidMinutes("10"))
        assertEquals(180, parseValidMinutes("180"))
    }

    @Test
    fun justOutsideBoundaries_areInvalid() {
        assertNull(parseValidMinutes("9"))
        assertNull(parseValidMinutes("181"))
    }

    @Test
    fun midRangeValues_areValid() {
        assertEquals(45, parseValidMinutes("45"))
        assertEquals(60, parseValidMinutes("60"))
        assertEquals(61, parseValidMinutes("61"))
        assertEquals(90, parseValidMinutes("90"))
        assertEquals(120, parseValidMinutes("120"))
    }

    @Test
    fun leadingZeros_areParsedNormally() {
        assertEquals(45, parseValidMinutes("045"))
        assertEquals(10, parseValidMinutes("010"))
        assertEquals(180, parseValidMinutes("0180"))
    }

    @Test
    fun leadingPlusSign_isAcceptedByKotlinStdlib() {
        // Documents actual toIntOrNull() behavior: a leading '+' is legal.
        assertEquals(45, parseValidMinutes("+45"))
        assertEquals(10, parseValidMinutes("+10"))
    }

    // ---------- isNumeric() ----------

    @Test
    fun isNumeric_trueOnlyForParsableIntegers() {
        assertTrue(isNumeric("45"))
        assertTrue(isNumeric("0"))
        assertTrue(isNumeric("-5")) // numeric, even though out of range
        assertTrue(isNumeric("99999")) // numeric, even though out of range

        assertFalse(isNumeric(""))
        assertFalse(isNumeric("abc"))
        assertFalse(isNumeric("10.5"))
        assertFalse(isNumeric(" "))
    }

    // ---------- isOutOfRange() ----------

    @Test
    fun isOutOfRange_trueForNumericButOutOfWindowValues() {
        assertTrue(isOutOfRange("9"))
        assertTrue(isOutOfRange("0"))
        assertTrue(isOutOfRange("-5"))
        assertTrue(isOutOfRange("181"))
        assertTrue(isOutOfRange("99999"))
    }

    @Test
    fun isOutOfRange_falseForInRangeValues() {
        assertFalse(isOutOfRange("10"))
        assertFalse(isOutOfRange("45"))
        assertFalse(isOutOfRange("180"))
    }

    @Test
    fun isOutOfRange_falseForNonNumericText() {
        // Not "out of range" -- it's not a number at all. isNumeric()
        // should be checked first for that distinction.
        assertFalse(isOutOfRange("abc"))
        assertFalse(isOutOfRange(""))
    }
}
