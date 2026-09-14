package com.priyanshu.mobilitylens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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