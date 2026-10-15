package com.priyanshusingh.campuseventexplorer.data

import androidx.compose.runtime.Immutable

/**
 * A single campus event.
 *
 * The class is intentionally a plain, immutable value type: it is never passed through
 * navigation (only its [id] is), so it does not need to be `Parcelable` or `@Serializable`.
 *
 * @property id Unique, stable identifier. Used as the `LazyColumn` key and as the only piece of
 * data carried by the detail route.
 * @property title Human readable event name.
 * @property category Short category label such as "Workshop" or "Social".
 * @property location Where the event takes place (building + room, or an outdoor spot).
 * @property time Human readable date/time such as "Tuesday 6:30 PM".
 * @property description Full, multi-sentence description shown on the detail screen.
 */
@Immutable
data class CampusEvent(
    val id: Long,
    val title: String,
    val category: String,
    val location: String,
    val time: String,
    val description: String,
)
