package com.example.tennismobileapp.ui.court.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CourtDetailRoute(
    courtId: Long,
    onBackClick: () -> Unit,
    viewModel: CourtDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(courtId) {
        viewModel.loadCourt(courtId)
    }

    CourtDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick
    )
}