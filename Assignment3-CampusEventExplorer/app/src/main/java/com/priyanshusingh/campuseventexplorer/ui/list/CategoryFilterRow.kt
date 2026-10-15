package com.priyanshusingh.campuseventexplorer.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.priyanshusingh.campuseventexplorer.R
import com.priyanshusingh.campuseventexplorer.ui.TestTags

/**
 * Horizontal row of quick-filter chips, one per distinct category.
 *
 * Stateless: the caller decides which chip is [selectedCategory] (by comparing against the
 * current search text) and what tapping a chip does.
 */
@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val rowDescription = stringResource(R.string.category_filters)
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = rowDescription }
            .testTag(TestTags.CATEGORY_CHIP_ROW),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = categories, key = { it }) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategoryClick(category) },
                label = { Text(category) },
                modifier = Modifier.testTag(TestTags.categoryChip(category)),
            )
        }
    }
}
