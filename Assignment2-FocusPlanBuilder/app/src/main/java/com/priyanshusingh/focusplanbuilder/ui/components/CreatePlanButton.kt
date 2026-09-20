package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.ui.theme.FocusFieldShape

/**
 * The "Create plan" button -- the single place in this screen where the
 * warm amber accent color appears, deliberately, so it reads as *the*
 * action rather than one of several competing bright elements.
 *
 * [enabled] is passed in from the caller rather than computed here -- this
 * composable has no idea what makes a plan valid, and it should not. The
 * enabled/disabled decision (subject not blank AND minutes parses to a
 * value in 10..180) lives in FocusPlanRoute, computed fresh on every
 * recomposition from the current input, with no separate mutable Boolean
 * to fall out of sync.
 */
@Composable
fun CreatePlanButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = FocusFieldShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = "Create plan",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
