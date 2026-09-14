package com.priyanshu.mobilitylens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnalyzePanel(
    appNameInput: String,
    feedbackMessage: String,
    isFeedbackError: Boolean,
    currentIndex: Int,
    accentColor: Color,
    context: Context,
    focusManager: FocusManager,
    onAppNameChange: (String) -> Unit,
    onFeedbackChange: (String) -> Unit,
    onErrorChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "✏️ ${stringResource(R.string.analyze_button)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E)
            )
            Text(
                text = stringResource(R.string.analyze_helper),
                fontSize = 12.sp,
                color = Color(0xFF90A4AE),
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = appNameInput,
                onValueChange = {
                    onAppNameChange(it)
                    onFeedbackChange("")
                    onErrorChange(false)
                },
                label = { Text(stringResource(R.string.app_name_hint)) },
                isError = isFeedbackError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    focusedLabelColor = accentColor
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (appNameInput.isBlank()) {
                        onFeedbackChange(context.getString(R.string.blank_input_error))
                        onErrorChange(true)
                    } else {
                        onFeedbackChange(
                            context.getString(
                                R.string.analyze_success,
                                appNameInput.trim(),
                                currentIndex + 1
                            )
                        )
                        onErrorChange(false)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A2E))
            ) {
                Text(
                    stringResource(R.string.analyze_button),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            if (feedbackMessage.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isFeedbackError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                        )
                        .padding(14.dp)
                ) {
                    Text(
                        text = feedbackMessage,
                        fontSize = 13.sp,
                        color = if (isFeedbackError) Color(0xFFB71C1C) else Color(0xFF1B5E20),
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}