package com.priyanshusingh.campuseventexplorer.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.priyanshusingh.campuseventexplorer.ui.theme.categoryAccent

/**
 * Hero banner at the top of the detail screen: the event title on a soft gradient derived from
 * the event's category accent. Purely decorative - the category itself is listed (exactly once)
 * in the facts card below, so screen readers and tests are not given duplicate text.
 */
@Composable
fun DetailHeader(
    title: String,
    category: String,
    modifier: Modifier = Modifier,
) {
    val accent = categoryAccent(category)
    val gradient = Brush.linearGradient(
        colors = listOf(
            accent.container,
            accent.container.copy(alpha = 0.35f),
        ),
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .padding(horizontal = 20.dp, vertical = 28.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
