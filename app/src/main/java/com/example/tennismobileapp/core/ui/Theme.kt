package com.example.tennismobileapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.tennismobileapp.core.ui.AppShapes
import com.example.tennismobileapp.core.ui.AppTypography
import com.example.tennismobileapp.core.ui.BackgroundLight
import com.example.tennismobileapp.core.ui.GreenPrimary
import com.example.tennismobileapp.core.ui.GreenSecondary
import com.example.tennismobileapp.core.ui.TextPrimary

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    secondary = GreenSecondary,
    background = BackgroundLight,
    surface = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun TennisAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
