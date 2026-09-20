package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.priyanshusingh.focusplanbuilder.model.MAX_MINUTES
import com.priyanshusingh.focusplanbuilder.model.MIN_MINUTES
import com.priyanshusingh.focusplanbuilder.model.MinutesValidation
import com.priyanshusingh.focusplanbuilder.model.errorMessage
import com.priyanshusingh.focusplanbuilder.model.minutesPreview
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanTestTags
import com.priyanshusingh.focusplanbuilder.ui.theme.FieldShape

/**
 * The available-minutes field.
 *
 * The value is a **String**, never an Int, because a text field has to be able
 * to hold every intermediate thing a user can type: nothing, a lone "1" on the
 * way to "15", or even "abc" if the keyboard lets them. None of those fit in an
 * Int. The safe conversion (`toIntOrNull()`) happens in FocusPlanRoute.
 *
 * `KeyboardType.Number` requests the numeric keyboard, but that is only a hint
 * to the IME (some keyboards still allow letters, and text can be pasted), so
 * validation never assumes the text is numeric.
 *
 * The supporting text does triple duty: default hint, live preview of the
 * category once the number is valid, or a specific error message.
 *
 * Pressing the keyboard's Done action creates the plan when the input is valid,
 * and otherwise just dismisses the keyboard.
 */
@Composable
fun MinutesInputField(
    value: String,
    onValueChange: (String) -> Unit,
    validation: MinutesValidation,
    canCreatePlan: Boolean,
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val errorMessage = validation.errorMessage()
    val preview = minutesPreview((validation as? MinutesValidation.Valid)?.minutes)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Minutes available ($MIN_MINUTES–$MAX_MINUTES)") },
        placeholder = { Text("45") },
        suffix = { Text("min") },
        supportingText = {
            Text(
                text = errorMessage
                    ?: preview
                    ?: "Whole minutes only, from $MIN_MINUTES to $MAX_MINUTES.",
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    errorMessage != null -> MaterialTheme.colorScheme.error
                    preview != null -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear minutes"
                    )
                }
            }
        },
        isError = errorMessage != null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                if (canCreatePlan) onCreatePlan()
            }
        ),
        shape = FieldShape,
        colors = focusFieldColors(),
        modifier = modifier
            .fillMaxWidth()
            .testTag(FocusPlanTestTags.MINUTES_FIELD)
    )
}
