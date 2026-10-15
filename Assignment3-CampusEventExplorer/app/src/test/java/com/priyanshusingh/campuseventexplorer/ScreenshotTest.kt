package com.priyanshusingh.campuseventexplorer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.navigation.EventDetail
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/**
 * Renders the full app with Robolectric's native (Skia) graphics backend and writes PNGs to
 * `app/build/screenshots/`. Doubles as a smoke test (every screen renders without throwing) and
 * as the reproducible source of the README images:
 *
 * ```
 * ./gradlew :app:testDebugUnitTest --tests "*ScreenshotTest*"
 * ```
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w411dp-h914dp-xxhdpi")
class ScreenshotTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    private val outputDir: File by lazy {
        File(System.getProperty("screenshotDir") ?: "build/screenshots").apply { mkdirs() }
    }

    private fun launchApp() {
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CampusEventExplorerApp(navController = navController, dynamicColor = false)
        }
    }

    private fun capture(name: String) {
        composeRule.waitForIdle()
        val view: View = composeRule.activity.window.decorView
        assertTrue("window has no size", view.width > 0 && view.height > 0)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        composeRule.runOnUiThread { view.draw(Canvas(bitmap)) }
        File(outputDir, "$name.png").outputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
    }

    @Test
    fun captureListScreen() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.LIST_SCREEN)
        capture("01_event_list")
    }

    @Test
    fun captureFilteredListScreen() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("tech")
        capture("02_event_list_filtered")
    }

    @Test
    fun captureEmptySearchState() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.SEARCH_FIELD).performTextInput("quantum")
        capture("03_event_list_no_results")
    }

    @Test
    fun captureDetailScreen() {
        launchApp()
        composeRule.onNodeWithTag(TestTags.eventItem(1L)).performClick()
        composeRule.onNodeWithTag(TestTags.DETAIL_SCREEN)
        capture("04_event_detail")
    }

    @Test
    fun captureUnavailableScreen() {
        launchApp()
        composeRule.runOnUiThread { navController.navigate(EventDetail(eventId = 999L)) }
        composeRule.onNodeWithTag(TestTags.UNAVAILABLE_STATE)
        capture("05_event_unavailable")
    }
}
