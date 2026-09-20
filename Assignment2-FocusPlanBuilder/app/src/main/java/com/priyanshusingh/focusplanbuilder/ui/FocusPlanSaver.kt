package com.priyanshusingh.focusplanbuilder.ui

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.priyanshusingh.focusplanbuilder.model.FocusPlan

/**
 * Flattens a plan into Bundle-friendly primitives (String, Int, String, Int).
 * An absent plan becomes an empty list, which the Saver treats as "nothing to
 * store". Split out from the Saver so it can be unit-tested on the JVM.
 */
fun focusPlanToSavedList(plan: FocusPlan?): List<Any> =
    if (plan == null) emptyList()
    else listOf(plan.subject, plan.minutes, plan.category, plan.breakMinutes)

/**
 * Rebuilds a plan from [focusPlanToSavedList]'s output. Returns null for an
 * empty list and also for anything malformed, so a corrupt Bundle can only
 * ever cost the user the result card, never crash the app.
 */
fun focusPlanFromSavedList(saved: List<Any?>): FocusPlan? {
    if (saved.size != 4) return null
    val subject = saved[0] as? String ?: return null
    val minutes = saved[1] as? Int ?: return null
    val category = saved[2] as? String ?: return null
    val breakMinutes = saved[3] as? Int ?: return null
    return FocusPlan(subject, minutes, category, breakMinutes)
}

/**
 * Lets the generated [FocusPlan] ride along in `rememberSaveable`, so the
 * result card survives rotation too. The assignment only requires the two text
 * fields to survive; preserving the plan is the optional extra it mentions.
 *
 * Using a `Saver` avoids making FocusPlan `Parcelable`, which would drag the
 * kotlin-parcelize plugin and an Android dependency into an otherwise pure
 * Kotlin model class.
 */
val FocusPlanSaver: Saver<FocusPlan?, Any> = listSaver(
    save = { plan -> focusPlanToSavedList(plan) },
    restore = { saved -> focusPlanFromSavedList(saved) }
)
