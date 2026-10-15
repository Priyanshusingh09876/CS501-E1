package com.priyanshusingh.campuseventexplorer.ui.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.priyanshusingh.campuseventexplorer.R
import com.priyanshusingh.campuseventexplorer.ui.TestTags

/**
 * One-line "12 events" / "2 of 12 events match “tech”" / "No events match “x”" caption above
 * the list, so the user always knows whether a filter is active and how much it hid.
 *
 * @param query The *normalised* search text (already trimmed); empty means no filter.
 */
@Composable
fun ResultSummary(
    shownCount: Int,
    totalCount: Int,
    query: String,
    modifier: Modifier = Modifier,
) {
    val text = when {
        query.isEmpty() -> pluralStringResource(R.plurals.results_all, totalCount, totalCount)
        shownCount == 0 -> stringResource(R.string.results_none, query)
        else -> pluralStringResource(
            R.plurals.results_filtered,
            shownCount,
            shownCount,
            totalCount,
            query,
        )
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.testTag(TestTags.RESULT_SUMMARY),
    )
}
