package com.priyanshusingh.focusplanbuilder.model

/**
 * Validates a study-subject String.
 *
 * A subject is considered valid when it contains at least one
 * non-whitespace character. This means:
 *  - "" (empty string)        -> invalid
 *  - "   " (spaces only)      -> invalid
 *  - "\t" / "\n" (whitespace) -> invalid
 *  - "Kotlin"                 -> valid
 *  - "  Databases  "          -> valid (has real content; caller may trim
 *                                it separately before display/storage)
 *
 * Kotlin's own String.isNotBlank() already implements exactly this rule
 * (true iff the string is not empty and contains at least one character
 * that is not whitespace), which is why the assignment asks for it by
 * name. This wrapper exists so call sites read as intent ("is the subject
 * valid?") rather than a raw stdlib call, and so this single line is easy
 * to unit test and easy to point to when explaining the code.
 */
fun isSubjectValid(subject: String): Boolean = subject.isNotBlank()

/**
 * Produces the "cleaned" version of a subject for storage/display: leading
 * and trailing whitespace removed. Does not affect validity -- a subject
 * must already be valid (see [isSubjectValid]) before this is meaningful.
 */
fun cleanSubject(subject: String): String = subject.trim()
