package com.priyanshusingh.campuseventexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.share.EventSharer
import com.priyanshusingh.campuseventexplorer.ui.detail.EventDetailScreen

/**
 * State holder for the [EventDetail] destination.
 *
 * Receives only the [eventId] decoded from the route and resolves it against [events]. An id
 * with no match yields `null`, which [EventDetailScreen] renders as "Event unavailable" rather
 * than crashing.
 */
@Composable
fun EventDetailRoute(
    eventId: Long,
    events: List<CampusEvent>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val event: CampusEvent? = remember(eventId, events) {
        events.firstOrNull { it.id == eventId }
    }
    val context = LocalContext.current

    EventDetailScreen(
        event = event,
        onBack = onBack,
        onShare = {
            // `event` can only be null on the unavailable screen, which has no share button;
            // the safe call keeps the callback total regardless.
            event?.let { EventSharer.share(context, it) }
        },
        modifier = modifier,
    )
}
