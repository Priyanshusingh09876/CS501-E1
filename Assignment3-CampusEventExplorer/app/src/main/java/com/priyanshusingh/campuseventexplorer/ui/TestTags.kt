package com.priyanshusingh.campuseventexplorer.ui

/**
 * Semantic test tags shared between production code and the UI tests, so tests never depend on
 * visible copy that may be localised or reworded.
 */
object TestTags {
    const val LIST_SCREEN = "list_screen"
    const val SEARCH_FIELD = "search_field"
    const val CLEAR_SEARCH = "clear_search"
    const val CATEGORY_CHIP_ROW = "category_chip_row"
    const val RESULT_SUMMARY = "result_summary"
    const val EVENT_LIST = "event_list"
    const val EMPTY_STATE = "empty_state"

    const val DETAIL_SCREEN = "detail_screen"
    const val UP_BUTTON = "up_button"
    const val SHARE_BUTTON = "share_button"
    const val UNAVAILABLE_STATE = "unavailable_state"
    const val RETURN_TO_EVENTS = "return_to_events"

    fun eventItem(id: Long): String = "event_item_$id"
    fun categoryChip(category: String): String = "category_chip_$category"
}
