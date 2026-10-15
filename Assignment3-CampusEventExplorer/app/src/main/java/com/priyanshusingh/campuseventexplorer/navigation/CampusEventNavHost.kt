package com.priyanshusingh.campuseventexplorer.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.priyanshusingh.campuseventexplorer.data.CampusEvent
import com.priyanshusingh.campuseventexplorer.data.EventRepository

/** `campusevents://event/{eventId}` - lets `adb` / links open a specific event for testing. */
const val EVENT_DEEP_LINK_BASE = "campusevents://event"

private const val TRANSITION_MS = 300

/**
 * The single place where navigation is wired.
 *
 * Screens receive plain data and lambdas; this host translates those lambdas into
 * `navigate(...)` / `navigateUp()` calls and decodes route arguments with [toRoute].
 *
 * @param navController Injected so tests can supply a `TestNavHostController`.
 * @param events The application's event collection. Defaults to the real repository.
 */
@Composable
fun CampusEventNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    events: List<CampusEvent> = EventRepository.events,
    categories: List<String> = EventRepository.categories,
) {
    NavHost(
        navController = navController,
        startDestination = EventList,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(TRANSITION_MS)) +
                fadeIn(tween(TRANSITION_MS))
        },
        exitTransition = { fadeOut(tween(TRANSITION_MS)) },
        popEnterTransition = { fadeIn(tween(TRANSITION_MS)) },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(TRANSITION_MS)) +
                fadeOut(tween(TRANSITION_MS))
        },
    ) {
        composable<EventList> {
            EventListRoute(
                events = events,
                categories = categories,
                onEventSelected = { eventId ->
                    // Pushes EventDetail on top of EventList: back stack becomes
                    // [EventList, EventDetail(eventId)]. Only the id is carried.
                    navController.navigateOnce(EventDetail(eventId = eventId))
                },
            )
        }

        composable<EventDetail>(
            deepLinks = listOf(navDeepLink<EventDetail>(basePath = EVENT_DEEP_LINK_BASE)),
        ) { backStackEntry ->
            val route: EventDetail = backStackEntry.toRoute()

            EventDetailRoute(
                eventId = route.eventId,
                events = events,
                onBack = { navController.returnToEventList() },
            )
        }
    }
}

/**
 * Up / "Return to events".
 *
 * In the normal flow the back stack is `[EventList, EventDetail]`, so this is a plain
 * [NavHostController.navigateUp] - identical to the system Back button.
 *
 * Edge case: when the detail screen was deep-linked into from *another* app's task, Navigation
 * places only `EventDetail` on the stack. `navigateUp()` would then finish this Activity and
 * relaunch the app; instead we show the list in place by navigating to [EventList] with a clean
 * stack, which is what "Return to events" promises.
 */
private fun NavHostController.returnToEventList() {
    if (!isCurrentEntryResumed) return
    if (previousBackStackEntry != null) {
        navigateUp()
    } else {
        navigate(EventList) {
            popUpTo(graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }
}
