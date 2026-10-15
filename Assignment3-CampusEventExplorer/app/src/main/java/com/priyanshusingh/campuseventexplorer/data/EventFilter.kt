package com.priyanshusingh.campuseventexplorer.data

/**
 * Pure filtering logic shared by the UI and the unit tests.
 *
 * The result is always *derived* from the current search text; nothing is cached or mutated.
 */
object EventFilter {

    /**
     * Normalises raw user input so that filtering is forgiving:
     * - leading / trailing whitespace is ignored ("  tech " == "tech")
     * - runs of inner whitespace are collapsed ("career   fair" == "career fair")
     */
    fun normalize(query: String): String = query.trim().replace(WHITESPACE, " ")

    /**
     * Returns the events whose **title or category** contains [query] (case-insensitive).
     *
     * A blank or whitespace-only query matches every event, which is what a user expects when
     * the search field is empty or they accidentally typed a space.
     */
    fun filter(events: List<CampusEvent>, query: String): List<CampusEvent> {
        val needle = normalize(query)
        if (needle.isEmpty()) return events
        return events.filter { event -> event.matches(needle) }
    }

    /** `true` when [needle] (already normalised) is found in the title or the category. */
    private fun CampusEvent.matches(needle: String): Boolean =
        title.contains(needle, ignoreCase = true) ||
            category.contains(needle, ignoreCase = true)

    private val WHITESPACE = Regex("\\s+")
}

/** Convenience extension so call sites read naturally: `events.filterByQuery(query)`. */
fun List<CampusEvent>.filterByQuery(query: String): List<CampusEvent> = EventFilter.filter(this, query)
