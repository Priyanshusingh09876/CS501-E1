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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanTestTags
import com.priyanshusingh.focusplanbuilder.ui.theme.FieldShape

/**
 * The study-subject field.
 *
 * Deliberately "dumb": it shows [value] and reports each change through
 * [onValueChange]. Whether the subject is *valid* is decided one level up in
 * FocusPlanRoute; this component only receives the outcome as [errorMessage].
 *
 * Pressing the keyboard's Next action moves focus straight to the minutes
 * field so the whole form can be filled without touching the screen.
 */
@Composable
fun SubjectInputField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val isError = errorMessage != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Study subject") },
        placeholder = { Text("e.g. Kotlin, Databases, Compose state") },
        supportingText = {
            Text(
                text = errorMessage ?: "What are you going to study?",
                style = MaterialTheme.typography.bodySmall
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear subject"
                    )
                }
            }
        },
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        shape = FieldShape,
        colors = focusFieldColors(),
        modifier = modifier
            .fillMaxWidth()
            .testTag(FocusPlanTestTags.SUBJECT_FIELD)
    )
}

/**
 * Shared field colors so the subject and minutes fields are visually
 * identical: a filled surface at rest, a strong primary ring when focused.
 */
@Composable
internal fun focusFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant
)
