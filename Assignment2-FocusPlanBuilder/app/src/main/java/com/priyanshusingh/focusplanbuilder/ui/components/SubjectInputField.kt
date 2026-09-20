package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.priyanshusingh.focusplanbuilder.ui.theme.FocusFieldShape

/**
 * The study-subject input field.
 *
 * This composable is intentionally "dumb": it receives the current [value]
 * and reports every change through [onValueChange]. It does not decide
 * whether the subject is valid -- that decision belongs to
 * FocusPlanRoute (see model/SubjectValidation.kt) -- this component only
 * displays whatever it is told to display.
 */
@Composable
fun SubjectInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Study subject") },
        placeholder = { Text("Kotlin, Databases, Compose state...") },
        singleLine = true,
        shape = FocusFieldShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.fillMaxWidth()
    )
}
