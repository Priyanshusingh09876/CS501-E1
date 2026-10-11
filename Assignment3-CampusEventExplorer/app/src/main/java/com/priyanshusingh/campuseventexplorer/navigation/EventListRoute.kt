package com.priyanshusingh.campuseventexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.filterByQuery
import com.priyanshusingh.campuseventexplorer.ui.list.EventListScreen

/**
 * State holder for the [EventList] destination. **This composable owns the search state.**
 *
 * - `query` lives in [rememberSaveable], so it survives rotation, dark-mode switches, process
 *   death and (because it is scoped to the `EventList` back-stack entry) a round trip to the
 *   detail screen and back.
 * - The visible list is *derived* on every recomposition from `events` + `query` via
 *   [remember] keyed on both; there is no second mutable list that could drift out of sync.
 * - [EventListScreen] below stays completely stateless and never sees a NavController.
 */
@Composable
fun EventListRoute(
    events: List<CampusEvent>,
    categories: List<String>,
    onEventSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }

    val filteredEvents = remember(events, query) { events.filterByQuery(query) }

    EventListScreen(
        events = filteredEvents,
        query = query,
        onQueryChange = { query = it },
        onEventSelected = onEventSelected,
        modifier = modifier,
        totalEventCount = events.size,
        categories = categories,
    )
}
