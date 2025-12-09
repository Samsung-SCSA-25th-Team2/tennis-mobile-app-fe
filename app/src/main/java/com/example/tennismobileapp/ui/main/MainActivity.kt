package com.example.tennismobileapp.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tennismobileapp.ui.theme.TennisAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 앱 UI는 모두 여기 들어감

            TennisAppTheme {
                // 앱 전체 테마를 적용하는 역할

                MainScreen()
                // SPA
            }
        }
    }
}
