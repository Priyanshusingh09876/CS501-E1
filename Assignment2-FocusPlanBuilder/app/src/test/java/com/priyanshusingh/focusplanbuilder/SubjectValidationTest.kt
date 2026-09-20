package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.cleanSubject
import com.priyanshusingh.focusplanbuilder.model.isSubjectValid
import com.priyanshusingh.focusplanbuilder.model.isSubjectWhitespaceOnly
import com.priyanshusingh.focusplanbuilder.model.subjectErrorMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for the subject rules (model/SubjectValidation.kt).
 */
class SubjectValidationTest {

    @Test
    fun emptyString_isInvalid() {
        assertFalse(isSubjectValid(""))
    }

    @Test
    fun spacesOnly_isInvalid() {
        assertFalse(isSubjectValid(" "))
        assertFalse(isSubjectValid("     "))
    }

    @Test
    fun otherWhitespaceOnly_isInvalid() {
        assertFalse(isSubjectValid("\t"))
        assertFalse(isSubjectValid("\n"))
        assertFalse(isSubjectValid("\t \n \r"))
        assertFalse(isSubjectValid("\u00A0")) // non-breaking space is whitespace too
    }

    @Test
    fun assignmentExamples_areValid() {
        assertTrue(isSubjectValid("Kotlin"))
        assertTrue(isSubjectValid("Databases"))
        assertTrue(isSubjectValid("Compose state"))
    }

    @Test
    fun singleCharacter_isValid() {
        assertTrue(isSubjectValid("a"))
        assertTrue(isSubjectValid("1"))
        assertTrue(isSubjectValid("!"))
    }

    @Test
    fun paddedText_isStillValid() {
        assertTrue(isSubjectValid("  Databases  "))
        assertTrue(isSubjectValid(" Kotlin"))
        assertTrue(isSubjectValid("Kotlin "))
    }

    @Test
    fun unicodeAndEmoji_areValid() {
        assertTrue(isSubjectValid("Álgebra"))
        assertTrue(isSubjectValid("数据库"))
        assertTrue(isSubjectValid("\uD83D\uDCDA"))
    }

    @Test
    fun veryLongSubject_isValid() {
        assertTrue(isSubjectValid("a".repeat(10_000)))
    }

    // ------------------------------------------------------ whitespace-only

    @Test
    fun whitespaceOnly_isDetected_butEmptyIsNot() {
        assertTrue(isSubjectWhitespaceOnly(" "))
        assertTrue(isSubjectWhitespaceOnly("   \t"))
        assertFalse(isSubjectWhitespaceOnly(""))
        assertFalse(isSubjectWhitespaceOnly("Kotlin"))
        assertFalse(isSubjectWhitespaceOnly(" Kotlin "))
    }

    @Test
    fun errorMessage_onlyForWhitespaceOnly() {
        assertEquals("Subject can't be only spaces.", subjectErrorMessage("   "))
        assertNull(subjectErrorMessage(""))
        assertNull(subjectErrorMessage("Kotlin"))
    }

    // -------------------------------------------------------------- cleaning

    @Test
    fun cleanSubject_trimsEnds() {
        assertEquals("Databases", cleanSubject("  Databases  "))
        assertEquals("Kotlin", cleanSubject("\tKotlin\n"))
        assertEquals("Compose state", cleanSubject("Compose state"))
    }

    @Test
    fun cleanSubject_collapsesInternalWhitespaceRuns() {
        assertEquals("Compose state", cleanSubject("Compose   state"))
        assertEquals("Compose state", cleanSubject("  Compose \t state  "))
        assertEquals("a b c", cleanSubject("a  b   c"))
    }

    @Test
    fun cleanSubject_leavesAlreadyCleanTextAlone() {
        assertEquals("Kotlin", cleanSubject("Kotlin"))
        assertEquals("Compose State", cleanSubject("Compose State"))
    }

    @Test
    fun cleanSubject_ofWhitespaceOnly_isEmpty() {
        assertEquals("", cleanSubject("   "))
        assertEquals("", cleanSubject(""))
    }
}
