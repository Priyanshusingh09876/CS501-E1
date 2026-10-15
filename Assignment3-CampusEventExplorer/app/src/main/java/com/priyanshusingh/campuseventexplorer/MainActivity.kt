package com.priyanshusingh.campuseventexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Single-activity entry point. All screens are Compose destinations inside
 * [CampusEventExplorerApp]; the activity itself holds no UI state.
 *
 * Configuration changes (rotation) are deliberately *not* suppressed via `configChanges`, so the
 * activity is recreated and the app has to rely on `rememberSaveable` + the NavController's own
 * saved state to restore the search text and the current destination.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CampusEventExplorerApp()
        }
    }
}
