package com.priyanshusingh.campuseventexplorer.share

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.priyanshusingh.campuseventexplorer.MainActivity
import com.priyanshusingh.campuseventexplorer.data.EventRepository
import com.priyanshusingh.campuseventexplorer.ui.TestTags
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Verifies that tapping "Share event" fires the system chooser wrapping an *implicit*
 * `ACTION_SEND` text intent whose text contains title, time and location.
 */
@RunWith(AndroidJUnit4::class)
class ShareIntentTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        Intents.init()
        // Stub every outgoing intent so the real share sheet never opens during the test.
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun shareButtonLaunchesChooserWithImplicitSendIntent() {
        val event = EventRepository.events.first()

        composeRule.onNodeWithTag(TestTags.eventItem(event.id)).performClick()
        composeRule.onNodeWithTag(TestTags.SHARE_BUTTON).performScrollTo().performClick()
        composeRule.waitForIdle()

        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(
                    equalTo(Intent.EXTRA_INTENT),
                    allOf(
                        hasAction(Intent.ACTION_SEND),
                        hasType(EventSharer.MIME_TYPE_TEXT),
                        hasExtra(equalTo(Intent.EXTRA_TEXT), containsString(event.title)),
                        hasExtra(equalTo(Intent.EXTRA_TEXT), containsString(event.time)),
                        hasExtra(equalTo(Intent.EXTRA_TEXT), containsString(event.location)),
                        hasExtra(Intent.EXTRA_SUBJECT, event.title),
                    ),
                ),
            ),
        )
    }
}
