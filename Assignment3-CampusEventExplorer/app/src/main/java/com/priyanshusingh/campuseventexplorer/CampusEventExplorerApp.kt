package com.priyanshusingh.campuseventexplorer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.priyanshusingh.campuseventexplorer.navigation.CampusEventNavHost
import com.priyanshusingh.campuseventexplorer.ui.theme.CampusEventExplorerTheme

/**
 * Root composable: theme + background surface + navigation graph.
 *
 * [navController] is a parameter (defaulting to [rememberNavController]) so UI tests can
 * observe navigation with a `TestNavHostController` without touching production code.
 */
@Composable
fun CampusEventExplorerApp(
    navController: NavHostController = rememberNavController(),
    dynamicColor: Boolean = true,
) {
    CampusEventExplorerTheme(dynamicColor = dynamicColor) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            CampusEventNavHost(navController = navController)
        }
    }
}
