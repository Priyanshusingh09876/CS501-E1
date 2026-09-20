package com.priyanshusingh.focusplanbuilder.model

/**
 * Recommends a break length, in minutes, for a given study duration.
 *
 * Study duration       Recommended break
 * 10-29                5
 * 30-60                10
 * > 60                 15
 *
 * For durations below 10 -- which the UI itself never allows through,
 * since the Create Plan button stays disabled for those values -- this
 * returns 0, signaling "not applicable" rather than a misleading positive
 * number or a thrown exception. This keeps the function total (defined for
 * every possible Int) and therefore safe to call from a test or any future
 * caller without first re-checking validity.
 */
fun recommendedBreak(minutes: Int): Int = when {
    minutes < 10 -> 0
    minutes in 10..29 -> 5
    minutes in 30..60 -> 10
    else -> 15 // covers everything above 60, no upper bound
}
