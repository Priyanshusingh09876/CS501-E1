package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.priyanshusingh.focusplanbuilder.ui.theme.FocusFieldShape

/**
 * The available-minutes input field.
 *
 * Stores its value as a [String] -- never an Int -- because a TextField's
 * value must be able to represent every intermediate state the user can
 * type into it: empty text, a partial number, or even non-numeric garbage
 * like "abc". An Int cannot represent any of those, so String is the only
 * type that can hold "whatever is currently in the box" without crashing
 * or silently dropping keystrokes. Converting to an actual Int (safely,
 * via toIntOrNull()) happens one layer up, in FocusPlanRoute.
 *
 * `keyboardType = KeyboardType.Number` requests the numeric keyboard, as
 * required, but this is a *hint* to the IME only -- it does not, by
 * itself, prevent non-numeric text from ending up in [value] (some
 * keyboards still allow switching back to a full alphabet layout). That is
 * exactly why validation must still happen with toIntOrNull() rather than
 * being skipped on the assumption that only digits can arrive here.
 */
@Composable
fun MinutesInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Minutes available (10-180)") },
        placeholder = { Text("45") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = FocusFieldShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.fillMaxWidth()
    )
}
