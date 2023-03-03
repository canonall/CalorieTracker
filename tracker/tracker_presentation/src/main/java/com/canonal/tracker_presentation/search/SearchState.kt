package com.canonal.tracker_presentation.search

import com.canonal.tracker_domain.model.TrackableFood

data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val trackableFoodStateList: List<TrackableFoodUiState> = emptyList()
)

// each TrackableFood can be expanded and has specific amount
data class TrackableFoodUiState(
    val trackableFood: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)
