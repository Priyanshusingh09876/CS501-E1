package com.priyanshusingh.focusplanbuilder.model

/**
 * Validates the study subject.
 *
 * A subject is valid when it contains at least one non-whitespace character.
 * Kotlin's `isNotBlank()` implements exactly that rule, which is why the
 * assignment names it:
 *
 * ```
 * isSubjectValid("")               // false
 * isSubjectValid("   ")            // false
 * isSubjectValid("\t\n")           // false
 * isSubjectValid("Kotlin")         // true
 * isSubjectValid("  Databases  ")  // true (real content, just padded)
 * ```
 */
fun isSubjectValid(subject: String): Boolean = subject.isNotBlank()

/**
 * True when the user has typed *something* but it is all whitespace. This is
 * the only subject state worth flagging as an error: an empty field is simply
 * "not started yet", not a mistake.
 */
fun isSubjectWhitespaceOnly(subject: String): Boolean =
    subject.isNotEmpty() && subject.isBlank()

/**
 * Produces the "cleaned" subject stored in the plan and shown on the card:
 * leading/trailing whitespace removed and any run of internal whitespace
 * collapsed to a single space, so "  Compose   state " becomes "Compose state".
 */
fun cleanSubject(subject: String): String =
    subject.trim().replace(INTERNAL_WHITESPACE, " ")

/** Error text for the subject field, or null when there is nothing to report. */
fun subjectErrorMessage(subject: String): String? =
    if (isSubjectWhitespaceOnly(subject)) "Subject can't be only spaces." else null

private val INTERNAL_WHITESPACE = Regex("\\s+")
