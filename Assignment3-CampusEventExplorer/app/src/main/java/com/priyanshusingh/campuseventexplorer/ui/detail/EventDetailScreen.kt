package com.priyanshusingh.campuseventexplorer.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshusingh.campuseventexplorer.R
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.components.CategoryBadge
import com.priyanshusingh.campuseventexplorer.ui.theme.CampusEventExplorerTheme

/**
 * Detail destination UI.
 *
 * Receives the already-resolved [event] (or `null` when the route's id matched nothing) plus two
 * callbacks. It knows nothing about navigation or intents: [onBack] is wired by the NavHost to
 * `navigateUp()` and [onShare] to the implicit `ACTION_SEND` intent.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    event: CampusEvent?,
    onBack: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.DETAIL_SCREEN),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = event?.title ?: stringResource(R.string.detail_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag(TestTags.UP_BUTTON),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up),
                        )
                    }
                },
                actions = {
                    if (event != null) {
                        IconButton(onClick = onShare) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.share_event),
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)

        if (event == null) {
            EventUnavailableContent(
                onReturnToEvents = onBack,
                modifier = contentModifier,
            )
        } else {
            EventDetailContent(
                event = event,
                onShare = onShare,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun EventDetailContent(
    event: CampusEvent,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DetailHeader(title = event.title, category = event.category)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                DetailRow(
                    icon = Icons.Default.Category,
                    label = stringResource(R.string.label_category),
                ) {
                    CategoryBadge(category = event.category)
                }
                DetailRow(
                    icon = Icons.Default.Schedule,
                    label = stringResource(R.string.label_time),
                ) {
                    Text(text = event.time, style = MaterialTheme.typography.bodyLarge)
                }
                DetailRow(
                    icon = Icons.Default.Place,
                    label = stringResource(R.string.label_location),
                ) {
                    Text(text = event.location, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        HorizontalDivider()

        Text(
            text = stringResource(R.string.label_about),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = event.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onShare,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag(TestTags.SHARE_BUTTON),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.share_event))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventDetailScreenPreview() {
    CampusEventExplorerTheme(dynamicColor = false) {
        EventDetailScreen(
            event = EventRepository.events.first(),
            onBack = {},
            onShare = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Invalid id")
@Composable
private fun EventDetailScreenUnavailablePreview() {
    CampusEventExplorerTheme(dynamicColor = false) {
        EventDetailScreen(
            event = null,
            onBack = {},
            onShare = {},
        )
    }
}
