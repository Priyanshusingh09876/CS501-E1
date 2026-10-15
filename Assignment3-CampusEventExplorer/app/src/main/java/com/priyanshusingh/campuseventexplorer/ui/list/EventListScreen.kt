package com.priyanshusingh.campuseventexplorer.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshusingh.campuseventexplorer.R
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.EventFilter
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.data.filterByQuery
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import com.priyanshusingh.campuseventexplorer.ui.components.EmptySearchState
import com.priyanshusingh.campuseventexplorer.ui.components.SearchField
import com.priyanshusingh.campuseventexplorer.ui.theme.CampusEventExplorerTheme

/**
 * Start destination UI: a search field, optional category quick-filters and the event list.
 *
 * This composable is **stateless** and never sees a `NavController`:
 * - [events] is the already-filtered list the caller derived from [query];
 * - [query] / [onQueryChange] hoist the search text to the caller (who keeps it in
 *   `rememberSaveable`);
 * - [onEventSelected] reports the id of the tapped event and nothing else.
 *
 * @param totalEventCount Size of the unfiltered collection, used for the "3 of 12" summary.
 * @param categories Distinct categories rendered as quick-filter chips. Tapping a chip simply
 * sets the search text to that category, so there is still exactly one source of truth.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    events: List<CampusEvent>,
    query: String,
    onQueryChange: (String) -> Unit,
    onEventSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    totalEventCount: Int = events.size,
    categories: List<String> = emptyList(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val normalizedQuery = EventFilter.normalize(query)

    // LazyColumn keeps its scroll position *by key*, so after editing the search the list could
    // stay scrolled to whichever item happened to be first before. Jump back to the top whenever
    // the effective query actually changes - but not on rotation/restoration, where the query is
    // unchanged and the restored scroll offset should be kept.
    var lastAppliedQuery by rememberSaveable { mutableStateOf(normalizedQuery) }
    LaunchedEffect(normalizedQuery) {
        if (lastAppliedQuery != normalizedQuery) {
            lastAppliedQuery = normalizedQuery
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(TestTags.LIST_SCREEN),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.list_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
        ) {
            SearchField(
                query = query,
                onQueryChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            if (categories.isNotEmpty()) {
                CategoryFilterRow(
                    categories = categories,
                    selectedCategory = categories.firstOrNull { it.equals(normalizedQuery, ignoreCase = true) },
                    onCategoryClick = { category ->
                        // Tapping the already-selected chip clears the filter; otherwise the chip
                        // simply types the category into the search box for the user.
                        if (category.equals(normalizedQuery, ignoreCase = true)) {
                            onQueryChange("")
                        } else {
                            onQueryChange(category)
                        }
                    },
                )
            }

            ResultSummary(
                shownCount = events.size,
                totalCount = totalEventCount,
                query = normalizedQuery,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            )

            if (events.isEmpty()) {
                EmptySearchState(
                    query = query,
                    onClearSearch = { onQueryChange("") },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(TestTags.EVENT_LIST),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = events,
                        key = { event -> event.id },
                        contentType = { "event" },
                    ) { event ->
                        EventListItem(
                            event = event,
                            onClick = { onEventSelected(event.id) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventListScreenPreview() {
    var query by rememberSaveable { mutableStateOf("") }
    CampusEventExplorerTheme(dynamicColor = false) {
        EventListScreen(
            events = EventRepository.events.filterByQuery(query),
            query = query,
            onQueryChange = { query = it },
            onEventSelected = {},
            totalEventCount = EventRepository.events.size,
            categories = EventRepository.categories,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Empty state")
@Composable
private fun EventListScreenEmptyPreview() {
    CampusEventExplorerTheme(dynamicColor = false) {
        EventListScreen(
            events = emptyList(),
            query = "quantum",
            onQueryChange = {},
            onEventSelected = {},
            totalEventCount = EventRepository.events.size,
            categories = EventRepository.categories,
        )
    }
}
