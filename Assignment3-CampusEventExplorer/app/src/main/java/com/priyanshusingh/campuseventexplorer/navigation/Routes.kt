package com.priyanshusingh.campuseventexplorer.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes.
 *
 * Navigation Compose serialises these objects into the route string itself, so no route string
 * is ever built by hand and the argument type (`Long`) is checked by the compiler.
 */

/** Start destination: the searchable list of events. Carries no arguments. */
@Serializable
data object EventList

/**
 * Detail destination.
 *
 * Only the **identity** of the event travels through the back stack. The destination looks the
 * full [com.priyanshusingh.campuseventexplorer.data.CampusEvent] up again from the repository,
 * which keeps the saved state tiny and guarantees the screen always shows current data.
 */
@Serializable
data class EventDetail(
    val eventId: Long,
)
