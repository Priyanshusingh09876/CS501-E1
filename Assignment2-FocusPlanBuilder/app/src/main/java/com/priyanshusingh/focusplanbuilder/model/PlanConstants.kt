package com.priyanshusingh.focusplanbuilder.model

/**
 * Minimum valid study duration, inclusive.
 * Pulled into a named constant so validation, the button-enabled check,
 * and documentation/tests all reference a single source of truth instead
 * of scattering the literal `10` around the codebase.
 */
const val MIN_MINUTES: Int = 10

/**
 * Maximum valid study duration, inclusive.
 */
const val MAX_MINUTES: Int = 180
