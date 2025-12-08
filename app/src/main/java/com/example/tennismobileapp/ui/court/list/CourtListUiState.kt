package com.example.tennismobileapp.ui.court.list

import com.example.tennismobileapp.ui.court.component.CourtItemUiModel

data class CourtListUiState(
    val isLoading: Boolean = false,
    val courtList: List<CourtItemUiModel> = emptyList(),
    val errorMessage: String? = null
)
