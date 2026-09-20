package com.priyanshusingh.focusplanbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanRoute
import com.priyanshusingh.focusplanbuilder.ui.theme.FocusPlanBuilderTheme

/**
 * The only Activity in the app. Everything on screen is Jetpack Compose: no
 * XML layouts, no legacy Views, no Fragments.
 *
 * `enableEdgeToEdge()` lets the parchment background run under the system
 * bars; `Scaffold` then hands back the safe insets as padding so nothing is
 * drawn under the status bar or navigation bar.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusPlanBuilderTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    FocusPlanRoute(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
