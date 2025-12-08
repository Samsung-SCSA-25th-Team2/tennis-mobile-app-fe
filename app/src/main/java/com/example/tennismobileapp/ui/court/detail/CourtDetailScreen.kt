package com.example.tennismobileapp.ui.court.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tennismobileapp.core.ui.AppTypography
import com.example.tennismobileapp.ui.court.component.CourtThumbnail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDetailScreen(
    uiState: CourtDetailUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("테니스장 상세") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // 로딩중
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // 에러 메시지 있음
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // 정상
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        CourtThumbnail(
                            thumbnailUrl = uiState.thumbnail.orEmpty(),
                            contentDescription = uiState.courtName
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = uiState.courtName,
                            style = AppTypography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = uiState.address,
                            style = AppTypography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.latitude != null && uiState.longitude != null) {
                            Text(
                                text = "위도: ${uiState.latitude}, 경도: ${uiState.longitude}",
                                style = AppTypography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
