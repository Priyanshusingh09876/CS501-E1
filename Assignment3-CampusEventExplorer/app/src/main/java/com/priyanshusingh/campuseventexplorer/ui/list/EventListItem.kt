package com.priyanshusingh.campuseventexplorer.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshusingh.campuseventexplorer.R
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.components.CategoryBadge
import com.priyanshusingh.campuseventexplorer.ui.components.EventMetaRow
import com.priyanshusingh.campuseventexplorer.ui.theme.CampusEventExplorerTheme
import com.priyanshusingh.campuseventexplorer.ui.theme.categoryAccent

/**
 * One row of the event list: title, category, location and time.
 *
 * A thin stripe on the leading edge carries the category's accent colour, so a user can scan
 * the list by colour before reading a single word.
 *
 * The whole card is a single accessibility node (`mergeDescendants`) with a descriptive label,
 * so TalkBack reads "Mobile Development Meetup. Tech at CAS 214, Tuesday 6:30 PM. Opens event
 * details." as one item instead of four separate texts.
 */
@Composable
fun EventListItem(
    event: CampusEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accessibilityLabel = stringResource(
        R.string.event_card_description,
        event.title,
        event.category,
        event.location,
        event.time,
    )
    val accent = categoryAccent(event.category)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag(TestTags.eventItem(event.id))
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = accessibilityLabel
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Category accent stripe along the leading edge.
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accent.onContainer.copy(alpha = 0.55f)),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp, top = 14.dp, end = 4.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                CategoryBadge(category = event.category)
                Spacer(Modifier.height(2.dp))
                EventMetaRow(icon = Icons.Default.Place, text = event.location)
                EventMetaRow(icon = Icons.Default.Schedule, text = event.time)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListItemPreview() {
    CampusEventExplorerTheme(dynamicColor = false) {
        EventListItem(
            event = EventRepository.events.first(),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
