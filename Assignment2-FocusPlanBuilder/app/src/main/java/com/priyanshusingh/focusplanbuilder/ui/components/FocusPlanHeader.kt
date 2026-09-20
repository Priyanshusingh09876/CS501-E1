package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The screen title. Its own composable so FocusPlanScreen's top-level body
 * reads as a short list of named sections rather than one long block.
 */
@Composable
fun FocusPlanTitle(modifier: Modifier = Modifier) {
    Text(
        text = "Build a focus plan",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

/**
 * The short instructional copy shown under the title. Written from the
 * user's point of view, in plain sentence case -- no all-caps eyebrow
 * label above it, since a single clear sentence does the same job without
 * the generic template chrome.
 */
@Composable
fun FocusPlanInstructions(modifier: Modifier = Modifier) {
    Text(
        text = "Tell us what you're studying and how much time you have. " +
            "We'll turn it into a short plan with a break built in.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}
