package com.example.tennismobileapp.ui.court.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * CourtDetailRoute는 “네비게이션 인자(courtId)를 기준으로 ViewModel에게 데이터를 로딩시키고,
 * 그 상태를 UI에 전달해 주는 중간 조립 지점”이야.
 *
 * - CourtDetailScreen은 오직 UI만 그리도록 설계
 * - CourtDetailScreen은 테스트도 편하고, 다른 곳에서 재사용도 쉬움
 */
@Composable
fun CourtDetailRoute(
    courtId: Long,
    onBackClick: () -> Unit,
    // AppNavGraph에서 받음

    viewModel: CourtDetailViewModel = viewModel()
    // viewModel 객체 가져오기
) {
    val uiState by viewModel.uiState.collectAsState()
    // Compose UI가 ViewModel의 state 관찰중

    // *****
    LaunchedEffect(courtId) {
        // "이 화면이 처음 열릴 때(or courtId가 바뀔 때) 한 번만 데이터를 로딩해라"
        // 를 보장해주는 장치가 LaunchedEffect(courtId)임.

        viewModel.loadCourt(courtId)
    }

    CourtDetailScreen(
        // 화면 그리기만 담당

        uiState = uiState,
        // uiState 넘김
        onBackClick = onBackClick
        // onBackClick 넘김 (콜백)
    )
}