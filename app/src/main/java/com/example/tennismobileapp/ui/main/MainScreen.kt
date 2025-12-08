package com.example.tennismobileapp.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tennismobileapp.ui.navigation.AppNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
    }
}
