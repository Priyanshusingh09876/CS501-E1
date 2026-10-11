package com.priyanshusingh.campuseventexplorer.share

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.priyanshusingh.campuseventexplorer.R
import com.priyanshusingh.campuseventexplorer.data.CampusEvent

/**
 * Builds and launches the implicit `ACTION_SEND` share intent for an event.
 *
 * Kept outside the composables so the screen stays a pure function of its inputs and the
 * intent logic can be unit-tested without Compose.
 */
object EventSharer {

    /** MIME type advertised to the system so only text-capable apps appear in the sheet. */
    const val MIME_TYPE_TEXT = "text/plain"

    /**
     * The exact text handed to the share sheet, e.g.
     *
     * ```
     * Mobile Development Meetup — Tuesday 6:30 PM — CAS 214
     *
     * Monthly meetup for students building ...
     * ```
     *
     * The first line always follows the `title — time — location` contract.
     */
    fun buildShareText(event: CampusEvent): String = buildString {
        append(buildSummaryLine(event))
        if (event.description.isNotBlank()) {
            append("\n\n")
            append(event.description.trim())
        }
    }

    /** Single-line summary: `title — time — location` (em dashes, as in the assignment). */
    fun buildSummaryLine(event: CampusEvent): String =
        listOf(event.title, event.time, event.location)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(separator = " \u2014 ")

    /**
     * Creates the *implicit* intent. No component or package is set: the action + MIME type
     * alone describe what we want, and Android resolves which apps can fulfil it.
     */
    fun createSendIntent(event: CampusEvent): Intent =
        Intent(Intent.ACTION_SEND).apply {
            type = MIME_TYPE_TEXT
            putExtra(Intent.EXTRA_SUBJECT, event.title)
            putExtra(Intent.EXTRA_TITLE, event.title)
            putExtra(Intent.EXTRA_TEXT, buildShareText(event))
        }

    /**
     * Wraps the send intent in the system chooser and starts it.
     *
     * Edge cases handled:
     * - Called with a non-Activity context: `FLAG_ACTIVITY_NEW_TASK` is added so Android does
     *   not throw.
     * - No activity can handle the request (extremely locked-down devices / profiles): the
     *   `ActivityNotFoundException` is caught and a toast is shown instead of crashing.
     */
    fun share(context: Context, event: CampusEvent) {
        val chooser = Intent.createChooser(
            createSendIntent(event),
            context.getString(R.string.share_event_chooser_title),
        )
        if (context !is Activity) {
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(chooser)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, R.string.share_unavailable, Toast.LENGTH_SHORT).show()
        }
    }
}
