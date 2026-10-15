package com.priyanshusingh.campuseventexplorer.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.priyanshusingh.campuseventexplorer.ui.theme.categoryAccent

/**
 * Small rounded pill that shows an event's category, tinted with that category's accent colour
 * (see [categoryAccent]) so the same category looks the same everywhere in the app.
 */
@Composable
fun CategoryBadge(
    category: String,
    modifier: Modifier = Modifier,
) {
    val accent = categoryAccent(category)
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = accent.container,
        contentColor = accent.onContainer,
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}
