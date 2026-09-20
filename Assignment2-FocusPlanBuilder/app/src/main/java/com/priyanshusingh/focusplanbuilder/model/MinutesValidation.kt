package com.priyanshusingh.focusplanbuilder.model

/**
 * Every possible outcome of validating the raw minutes text.
 *
 * Modelling the result as a sealed hierarchy (instead of a bare `Int?`) lets
 * the UI tell the user *why* the value is not accepted yet, while the button
 * logic still only needs to ask "is it [Valid]?".
 */
sealed interface MinutesValidation {

    /** Nothing typed yet, or whitespace only. Not an error, just incomplete. */
    data object Empty : MinutesValidation

    /** Text that `toIntOrNull()` could not turn into a whole number. */
    data object NotANumber : MinutesValidation

    /** A whole number, but below [MIN_MINUTES]. */
    data class TooShort(val minutes: Int) : MinutesValidation

    /** A whole number, but above [MAX_MINUTES]. */
    data class TooLong(val minutes: Int) : MinutesValidation

    /** A whole number inside the accepted window. */
    data class Valid(val minutes: Int) : MinutesValidation
}

/**
 * Validates the minutes text exactly the way the screen does.
 *
 * `toIntOrNull()` is used instead of `toInt()` on purpose: `toInt()` throws a
 * `NumberFormatException` for "", "abc", "10.5", or a number too large for an
 * Int, any of which would crash the app mid-keystroke. `toIntOrNull()` simply
 * returns null for all of those, which this function reports as [MinutesValidation.NotANumber].
 *
 * Examples:
 * ```
 * validateMinutes("")      // Empty
 * validateMinutes("abc")   // NotANumber
 * validateMinutes("10.5")  // NotANumber   (decimals are not whole minutes)
 * validateMinutes("9")     // TooShort(9)
 * validateMinutes("181")   // TooLong(181)
 * validateMinutes("45")    // Valid(45)
 * ```
 */
fun validateMinutes(minutesText: String): MinutesValidation {
    if (minutesText.isBlank()) return MinutesValidation.Empty

    val minutes: Int? = minutesText.toIntOrNull()

    return when {
        minutes == null -> MinutesValidation.NotANumber
        minutes < MIN_MINUTES -> MinutesValidation.TooShort(minutes)
        minutes > MAX_MINUTES -> MinutesValidation.TooLong(minutes)
        else -> MinutesValidation.Valid(minutes)
    }
}

/**
 * The parsed minutes when, and only when, the text is a whole number in
 * 10..180. Null in every other case. This is the value the button logic and
 * the plan factory use.
 */
fun parseValidMinutes(minutesText: String): Int? =
    (validateMinutes(minutesText) as? MinutesValidation.Valid)?.minutes

/** True when the text is a syntactically valid Int, regardless of range. */
fun isNumeric(minutesText: String): Boolean = minutesText.toIntOrNull() != null

/** True when the text is a number, but outside the accepted window. */
fun isOutOfRange(minutesText: String): Boolean =
    when (validateMinutes(minutesText)) {
        is MinutesValidation.TooShort, is MinutesValidation.TooLong -> true
        else -> false
    }

/**
 * A short, user-facing explanation for states that need one, or null when the
 * field is empty or valid. Kept in the model so it is unit-testable without
 * rendering any UI.
 */
fun MinutesValidation.errorMessage(): String? = when (this) {
    MinutesValidation.Empty -> null
    MinutesValidation.NotANumber -> "Enter whole minutes using digits only."
    is MinutesValidation.TooShort -> "Minimum is $MIN_MINUTES minutes. $minutes is too short."
    is MinutesValidation.TooLong -> "Maximum is $MAX_MINUTES minutes. $minutes is too long."
    is MinutesValidation.Valid -> null
}

/** True for any state that should render the field in its error style. */
fun MinutesValidation.isError(): Boolean = errorMessage() != null
