package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.cleanSubject
import com.priyanshusingh.focusplanbuilder.model.isSubjectValid
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for isSubjectValid() and cleanSubject() (model/SubjectValidation.kt).
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
        assertFalse(isSubjectValid("\t \n \t"))
    }

    @Test
    fun normalText_isValid() {
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
    fun textWithLeadingOrTrailingSpaces_isStillValid() {
        // Only *entirely* blank text is invalid; surrounding whitespace on
        // otherwise-real content does not make it blank.
        assertTrue(isSubjectValid("  Databases  "))
        assertTrue(isSubjectValid(" Kotlin"))
        assertTrue(isSubjectValid("Kotlin "))
    }

    @Test
    fun textWithInternalSpaces_isValid() {
        assertTrue(isSubjectValid("Compose   state")) // multiple internal spaces
    }

    @Test
    fun veryLongSubject_isValid() {
        val longSubject = "a".repeat(10_000)
        assertTrue(isSubjectValid(longSubject))
    }

    @Test
    fun cleanSubject_trimsLeadingAndTrailingWhitespace() {
        assertEquals("Databases", cleanSubject("  Databases  "))
        assertEquals("Compose state", cleanSubject("Compose state"))
        assertEquals("Kotlin", cleanSubject("\tKotlin\n"))
    }

    @Test
    fun cleanSubject_preservesInternalSpacing() {
        assertEquals("Compose   state", cleanSubject("  Compose   state  "))
    }
}
