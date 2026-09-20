package com.priyanshusingh.focusplanbuilder

import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.ui.focusPlanFromSavedList
import com.priyanshusingh.focusplanbuilder.ui.focusPlanToSavedList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for the flatten/restore helpers behind `FocusPlanSaver`
 * (ui/FocusPlanSaver.kt), which let the result card survive rotation.
 */
class FocusPlanSaverTest {

    private val plan = FocusPlan("Compose State", 45, "Focused session", 10)

    @Test
    fun roundTrip_restoresAnEqualPlan() {
        assertEquals(plan, focusPlanFromSavedList(focusPlanToSavedList(plan)))
    }

    @Test
    fun nullPlan_savesAsEmpty_andRestoresAsNull() {
        assertTrue(focusPlanToSavedList(null).isEmpty())
        assertNull(focusPlanFromSavedList(emptyList()))
    }

    @Test
    fun savedList_containsOnlyBundleFriendlyPrimitives() {
        val saved = focusPlanToSavedList(plan)
        assertEquals(listOf("Compose State", 45, "Focused session", 10), saved)
        assertTrue(saved.all { it is String || it is Int })
    }

    @Test
    fun malformedInput_restoresAsNull_insteadOfThrowing() {
        assertNull(focusPlanFromSavedList(listOf("only", "two")))
        assertNull(focusPlanFromSavedList(listOf("Kotlin", "45", "Focused session", "10"))) // wrong types
        assertNull(focusPlanFromSavedList(listOf(null, 45, "Focused session", 10)))
        assertNull(focusPlanFromSavedList(listOf("Kotlin", 45, "Focused session", 10, "extra")))
    }
}
