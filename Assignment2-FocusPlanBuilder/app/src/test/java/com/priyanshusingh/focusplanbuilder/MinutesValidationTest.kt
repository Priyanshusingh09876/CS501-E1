package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.MinutesValidation
import com.priyanshusingh.focusplanbuilder.model.errorMessage
import com.priyanshusingh.focusplanbuilder.model.isError
import com.priyanshusingh.focusplanbuilder.model.isNumeric
import com.priyanshusingh.focusplanbuilder.model.isOutOfRange
import com.priyanshusingh.focusplanbuilder.model.parseValidMinutes
import com.priyanshusingh.focusplanbuilder.model.validateMinutes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for the minutes parsing/validation layer (model/MinutesValidation.kt).
 * This is the code that keeps bad input from ever crashing the app, so it gets
 * the heaviest edge-case coverage in the project.
 */
class MinutesValidationTest {

    // ---------------------------------------------------------------- Empty

    @Test
    fun emptyOrWhitespace_isEmpty() {
        assertEquals(MinutesValidation.Empty, validateMinutes(""))
        assertEquals(MinutesValidation.Empty, validateMinutes(" "))
        assertEquals(MinutesValidation.Empty, validateMinutes("   "))
        assertEquals(MinutesValidation.Empty, validateMinutes("\t\n"))
    }

    @Test
    fun empty_isNotAnError_andHasNoMessage() {
        assertFalse(MinutesValidation.Empty.isError())
        assertNull(MinutesValidation.Empty.errorMessage())
    }

    // ----------------------------------------------------------- NotANumber

    @Test
    fun letters_areNotANumber_andDoNotThrow() {
        assertEquals(MinutesValidation.NotANumber, validateMinutes("abc"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("Kotlin"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("!!!"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("45abc"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("abc45"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("4a5"))
    }

    @Test
    fun decimals_areNotANumber() {
        // Whole minutes only: toIntOrNull() does not truncate "10.5" to 10.
        assertEquals(MinutesValidation.NotANumber, validateMinutes("10.5"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("45.0"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes(".5"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("45,5"))
    }

    @Test
    fun embeddedOrSurroundingWhitespace_isNotANumber() {
        assertEquals(MinutesValidation.NotANumber, validateMinutes(" 45"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("45 "))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("4 5"))
    }

    @Test
    fun loneSigns_andSymbols_areNotANumber() {
        assertEquals(MinutesValidation.NotANumber, validateMinutes("-"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("+"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("--5"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("1e2"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("0x2D"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("45min"))
    }

    @Test
    fun numbersTooBigForInt_areNotANumber_andDoNotThrow() {
        assertEquals(MinutesValidation.NotANumber, validateMinutes("2147483648")) // Int.MAX_VALUE + 1
        assertEquals(MinutesValidation.NotANumber, validateMinutes("-2147483649")) // Int.MIN_VALUE - 1
        assertEquals(MinutesValidation.NotANumber, validateMinutes("99999999999999999999"))
        assertEquals(MinutesValidation.NotANumber, validateMinutes("1".repeat(500)))
    }

    // ----------------------------------------------------- TooShort/TooLong

    @Test
    fun belowTen_isTooShort_withTheParsedValue() {
        assertEquals(MinutesValidation.TooShort(9), validateMinutes("9"))
        assertEquals(MinutesValidation.TooShort(0), validateMinutes("0"))
        assertEquals(MinutesValidation.TooShort(-1), validateMinutes("-1"))
        assertEquals(MinutesValidation.TooShort(-100), validateMinutes("-100"))
        assertEquals(MinutesValidation.TooShort(Int.MIN_VALUE), validateMinutes(Int.MIN_VALUE.toString()))
    }

    @Test
    fun aboveOneEighty_isTooLong_withTheParsedValue() {
        assertEquals(MinutesValidation.TooLong(181), validateMinutes("181"))
        assertEquals(MinutesValidation.TooLong(200), validateMinutes("200"))
        assertEquals(MinutesValidation.TooLong(999999), validateMinutes("999999"))
        assertEquals(MinutesValidation.TooLong(Int.MAX_VALUE), validateMinutes(Int.MAX_VALUE.toString()))
    }

    // ---------------------------------------------------------------- Valid

    @Test
    fun everyWholeNumberFromTenToOneEighty_isValid() {
        for (minutes in 10..180) {
            assertEquals("minutes=$minutes", MinutesValidation.Valid(minutes), validateMinutes(minutes.toString()))
        }
    }

    @Test
    fun exactBoundaries_areValid_andJustOutsideIsNot() {
        assertEquals(MinutesValidation.Valid(10), validateMinutes("10"))
        assertEquals(MinutesValidation.Valid(180), validateMinutes("180"))
        assertTrue(validateMinutes("9") is MinutesValidation.TooShort)
        assertTrue(validateMinutes("181") is MinutesValidation.TooLong)
    }

    @Test
    fun leadingZeros_parseNormally() {
        assertEquals(MinutesValidation.Valid(45), validateMinutes("045"))
        assertEquals(MinutesValidation.Valid(10), validateMinutes("0010"))
        assertEquals(MinutesValidation.Valid(180), validateMinutes("0180"))
    }

    @Test
    fun explicitPlusSign_isAcceptedByKotlin() {
        // Documents real toIntOrNull() behaviour rather than fighting it.
        assertEquals(MinutesValidation.Valid(45), validateMinutes("+45"))
    }

    // ------------------------------------------------------ parseValidMinutes

    @Test
    fun parseValidMinutes_returnsIntOnlyForValidInput() {
        assertEquals(10, parseValidMinutes("10"))
        assertEquals(45, parseValidMinutes("45"))
        assertEquals(180, parseValidMinutes("180"))

        assertNull(parseValidMinutes(""))
        assertNull(parseValidMinutes("abc"))
        assertNull(parseValidMinutes("9"))
        assertNull(parseValidMinutes("181"))
        assertNull(parseValidMinutes("10.5"))
        assertNull(parseValidMinutes("99999999999999999999"))
    }

    // --------------------------------------------------- isNumeric/isOutOfRange

    @Test
    fun isNumeric_isTrueForAnyParsableInt_regardlessOfRange() {
        assertTrue(isNumeric("45"))
        assertTrue(isNumeric("0"))
        assertTrue(isNumeric("-5"))
        assertTrue(isNumeric("99999"))

        assertFalse(isNumeric(""))
        assertFalse(isNumeric(" "))
        assertFalse(isNumeric("abc"))
        assertFalse(isNumeric("10.5"))
    }

    @Test
    fun isOutOfRange_isTrueOnlyForNumbersOutsideTheWindow() {
        assertTrue(isOutOfRange("9"))
        assertTrue(isOutOfRange("0"))
        assertTrue(isOutOfRange("-5"))
        assertTrue(isOutOfRange("181"))
        assertTrue(isOutOfRange("99999"))

        assertFalse(isOutOfRange("10"))
        assertFalse(isOutOfRange("45"))
        assertFalse(isOutOfRange("180"))
        assertFalse(isOutOfRange("abc")) // not a number at all, so not "out of range"
        assertFalse(isOutOfRange(""))
    }

    // ---------------------------------------------------------- Messages

    @Test
    fun errorMessages_mentionTheProblem() {
        assertEquals("Enter whole minutes using digits only.", MinutesValidation.NotANumber.errorMessage())
        assertEquals("Minimum is 10 minutes. 9 is too short.", MinutesValidation.TooShort(9).errorMessage())
        assertEquals("Maximum is 180 minutes. 181 is too long.", MinutesValidation.TooLong(181).errorMessage())
        assertNull(MinutesValidation.Valid(45).errorMessage())
    }

    @Test
    fun onlyProblemStates_areErrors() {
        assertTrue(MinutesValidation.NotANumber.isError())
        assertTrue(MinutesValidation.TooShort(3).isError())
        assertTrue(MinutesValidation.TooLong(300).isError())
        assertFalse(MinutesValidation.Valid(45).isError())
        assertFalse(MinutesValidation.Empty.isError())
        assertNotNull(validateMinutes("abc").errorMessage())
    }

    // ----------------------------------------------------------- No crash

    @Test
    fun fuzz_neverThrows_forAnyString() {
        val samples = listOf(
            "", " ", "\n", "0", "-0", "+0", "٤٥", "①", "1_000", "1,000", "NaN", "Infinity",
            "-", "+", "+-5", "5-", "٣٠", "１２", "12\u0000", "\uD83D\uDE00", "null", "true",
            "2147483647", "-2147483648", "9".repeat(1_000)
        )
        for (sample in samples) {
            // The assertion is simply "this line returns": no exception escapes.
            val result = validateMinutes(sample)
            assertNotNull("validateMinutes($sample) returned null", result)
        }
    }
}
