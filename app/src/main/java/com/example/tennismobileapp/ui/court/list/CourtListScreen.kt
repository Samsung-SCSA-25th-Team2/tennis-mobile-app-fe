package com.example.tennismobileapp.ui.court.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tennismobileapp.ui.court.component.CourtItemCard

@Composable
fun CourtListScreen(
    onCourtClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CourtListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            // 로딩중이면 로딩화면 출력
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 테니스장이 없고, 오류가 없으면
            uiState.courtList.isEmpty() && uiState.errorMessage == null -> {
                Text(
                    text = "등록된 테니스장이 없습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 오류가 있으면
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage!!,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 정상
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.courtList) { court ->
                        CourtItemCard(
                            court = court,
                            onClick = onCourtClick
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}