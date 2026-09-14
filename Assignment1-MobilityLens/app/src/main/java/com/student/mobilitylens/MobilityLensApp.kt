package com.priyanshu.mobilitylens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshu.mobilitylens.ui.theme.MobilityLensTheme

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
            AppHeader()
            Spacer(Modifier.height(12.dp))
            DimensionProgressBar(
                currentIndex = currentIndex,
                total = dimensions.size,
                accentColor = accentColor
            )
            Spacer(Modifier.height(12.dp))
            DimensionCard(
                dimension = dimensions[currentIndex],
                index = currentIndex,
                emoji = dimensionEmojis[currentIndex],
                accentColor = accentColor
            )
            Spacer(Modifier.height(16.dp))
            NavigationButtons(
                currentIndex = currentIndex,
                lastIndex = dimensions.lastIndex,
                accentColor = accentColor,
                onPrevious = {
                    currentIndex = (currentIndex - 1).coerceAtLeast(0)
                    feedbackMessage = ""
                    isFeedbackError = false
                },
                onNext = {
                    currentIndex = (currentIndex + 1).coerceAtMost(dimensions.lastIndex)
                    feedbackMessage = ""
                    isFeedbackError = false
                }
            )
            Spacer(Modifier.height(16.dp))
            AnalyzePanel(
                appNameInput = appNameInput,
                feedbackMessage = feedbackMessage,
                isFeedbackError = isFeedbackError,
                currentIndex = currentIndex,
                accentColor = accentColor,
                context = context,
                focusManager = focusManager,
                onAppNameChange = { appNameInput = it },
                onFeedbackChange = { feedbackMessage = it },
                onErrorChange = { isFeedbackError = it }
            )
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