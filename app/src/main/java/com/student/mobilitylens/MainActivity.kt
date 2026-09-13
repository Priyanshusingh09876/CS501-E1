package com.priyanshu.mobilitylens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.priyanshu.mobilitylens.ui.theme.MobilityLensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobilityLensTheme {
                MobilityLensApp()
            }
        }
    }
}

private fun buildDimensions(): List<MobilityDimension> = listOf(
    MobilityDimension(R.string.dim1_name, R.string.dim1_desc, R.string.dim1_impl),
    MobilityDimension(R.string.dim2_name, R.string.dim2_desc, R.string.dim2_impl),
    MobilityDimension(R.string.dim3_name, R.string.dim3_desc, R.string.dim3_impl),
    MobilityDimension(R.string.dim4_name, R.string.dim4_desc, R.string.dim4_impl),
    MobilityDimension(R.string.dim5_name, R.string.dim5_desc, R.string.dim5_impl),
    MobilityDimension(R.string.dim6_name, R.string.dim6_desc, R.string.dim6_impl)
)

val dimensionEmojis = listOf("👆", "📱", "⚡", "📍", "⏱️", "🔒")
val dimensionColors = listOf(
    Color(0xFF6C63FF),
    Color(0xFF00BCD4),
    Color(0xFFFF6B6B),
    Color(0xFF4CAF50),
    Color(0xFFFF9800),
    Color(0xFF9C27B0)
)

@Composable
fun MobilityLensApp() {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val dimensions = buildDimensions()
    var currentIndex by remember { mutableIntStateOf(0) }
    var appNameInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var isFeedbackError by remember { mutableStateOf(false) }

    val accentColor = dimensionColors[currentIndex]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .systemBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.app_intro),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                dimensions.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (index <= currentIndex) accentColor
                                else Color(0xFFDDE1F0)
                            )
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "${stringResource(R.string.dimension_label)} ${currentIndex + 1} ${stringResource(R.string.of_label)} ${dimensions.size}",
                fontSize = 12.sp,
                color = Color(0xFF90A4AE),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = dimensionEmojis[currentIndex], fontSize = 24.sp)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "${stringResource(R.string.dimension_label)} ${currentIndex + 1}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = stringResource(dimensions[currentIndex].nameRes),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1A2E)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = stringResource(dimensions[currentIndex].descRes),
                        fontSize = 14.sp,
                        color = Color(0xFF455A64),
                        lineHeight = 21.sp
                    )

                    Spacer(Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(accentColor.copy(alpha = 0.08f))
                            .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = stringResource(dimensions[currentIndex].implRes),
                            fontSize = 13.sp,
                            color = Color(0xFF37474F),
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        currentIndex = (currentIndex - 1).coerceAtLeast(0)
                        feedbackMessage = ""
                        isFeedbackError = false
                    },
                    enabled = currentIndex > 0,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1A1A2E))
                ) {
                    Text(stringResource(R.string.previous_button), fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = {
                        currentIndex = (currentIndex + 1).coerceAtMost(dimensions.lastIndex)
                        feedbackMessage = ""
                        isFeedbackError = false
                    },
                    enabled = currentIndex < dimensions.lastIndex,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        disabledContainerColor = Color(0xFFDDE1F0)
                    )
                ) {
                    Text(stringResource(R.string.next_button), fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(16.dp))

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
                            appNameInput = it
                            feedbackMessage = ""
                            isFeedbackError = false
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
                                feedbackMessage = context.getString(R.string.blank_input_error)
                                isFeedbackError = true
                            } else {
                                feedbackMessage = context.getString(
                                    R.string.analyze_success,
                                    appNameInput.trim(),
                                    currentIndex + 1
                                )
                                isFeedbackError = false
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

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Mobility Lens Preview")
@Composable
fun MobilityLensAppPreview() {
    MobilityLensTheme {
        MobilityLensApp()
    }
}