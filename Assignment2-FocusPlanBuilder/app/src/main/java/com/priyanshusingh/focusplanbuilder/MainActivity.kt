package com.priyanshusingh.focusplanbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanRoute
import com.priyanshusingh.focusplanbuilder.ui.theme.FocusPlanBuilderTheme

/**
 * The single Activity that hosts this single-screen application. All UI is
 * built with Jetpack Compose; there is no XML layout, no legacy View, and
 * no Fragment involved anywhere in this app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusPlanBuilderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FocusPlanRoute()
                }
            }
        }
    }
}
