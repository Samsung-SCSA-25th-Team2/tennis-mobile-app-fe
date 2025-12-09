package com.example.tennismobileapp.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tennismobileapp.ui.navigation.AppNavGraph

/**
 * 메인 화면에서 네비게이션을 위한 NavController를 생성하고,
 * Scaffold 구조 안에서 NavGraph를 전체 화면으로 렌더링하되,
 * 시스템 UI나 바와 겹치지 않도록 안전 패딩을 적용한다.
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // Compose Navigation 담당 객체
    // remember를 통해 recomposition에도 메모리가 초기화되지 않도록 함

    Scaffold { innerPadding ->
        // TopAppBar, BottomBar, FAB 등 을 고려한 레이아웃

        AppNavGraph(
            // Navigation Graph

            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
