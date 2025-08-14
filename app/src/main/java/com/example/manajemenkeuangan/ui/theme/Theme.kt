package com.example.manajemenkeuangan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrightBlue,
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF018786),
    background = White,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray
)

@Composable
fun ManajemenKeuanganTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
