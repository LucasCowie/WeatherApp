package com.example.weatherapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

private val default = Typography()

// Serif for the big text (temperatures, conditions, titles) for an old-book feel;
// small body text keeps the default font so it stays easy to read.
val Typography = Typography(
    displayMedium = default.displayMedium.copy(fontFamily = FontFamily.Serif),
    headlineMedium = default.headlineMedium.copy(fontFamily = FontFamily.Serif),
    headlineSmall = default.headlineSmall.copy(fontFamily = FontFamily.Serif),
    titleLarge = default.titleLarge.copy(fontFamily = FontFamily.Serif),
    titleMedium = default.titleMedium.copy(fontFamily = FontFamily.Serif)
)
