package com.example.tennismobileapp.ui.court.detail

data class CourtDetailUiState(
    val isLoading: Boolean = false,
    val courtName: String = "",
    val address: String = "",
    val thumbnail: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val errorMessage: String? = null
)
