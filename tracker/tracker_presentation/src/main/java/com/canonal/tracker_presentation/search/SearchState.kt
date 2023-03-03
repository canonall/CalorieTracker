package com.canonal.tracker_presentation.search

import com.canonal.tracker_domain.model.TrackableFood

data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val trackableFoodItemList: List<TrackableFoodItem> = emptyList()
)

// each TrackableFood can be expanded and has specific amount
data class TrackableFoodItem(
    val trackableFood: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)
