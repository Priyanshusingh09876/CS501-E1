package com.priyanshusingh.campuseventexplorer.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * `true` while the current destination is fully settled (not mid-transition / not already being
 * popped). Guarding navigation calls with this prevents the classic double-tap bugs:
 * two detail screens pushed from one fast double tap, or an extra pop that closes the app.
 */
val NavController.isCurrentEntryResumed: Boolean
    get() = currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

/**
 * Navigates to [route] only if the current entry is resumed, and with `launchSingleTop` so a
 * repeated request for the same destination never stacks duplicates.
 */
fun <T : Any> NavController.navigateOnce(
    route: T,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    if (!isCurrentEntryResumed) return
    navigate(route) {
        launchSingleTop = true
        builder()
    }
}
