package com.priyanshu.mobilitylens
/**
 * Holds the display content for one of the six mobility dimensions.
 *
 * @property nameRes      String resource ID for the dimension name.
 * @property descRes      String resource ID for the mobile constraint description.
 * @property implRes      String resource ID for the developer implication.
 */
data class MobilityDimension(
    val nameRes: Int,
    val descRes: Int,
    val implRes: Int
)
