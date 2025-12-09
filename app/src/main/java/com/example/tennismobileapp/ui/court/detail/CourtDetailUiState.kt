package com.example.tennismobileapp.ui.court.detail

/**
 * 단방향
 * ViewModel → UiState → Screen
 */
data class CourtDetailUiState(
    val isLoading: Boolean = false, // 데이터 가져오는 중인지 여부
    val courtName: String = "",
    val address: String = "",
    val thumbnail: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val errorMessage: String? = null
)
