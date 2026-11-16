package com.example.weatherapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// One dark "blood and ichor" look, whatever the phone's light/dark setting is.
// (Dynamic wallpaper colours are off on purpose: they would replace this palette.)
private val BloodColorScheme = darkColorScheme(
    primary = Ichor,                    // title, spinner, text field focus
    onPrimary = ClotBlack,
    primaryContainer = DeepBlood,       // top bar and bottom bar
    onPrimaryContainer = PaleIchor,
    secondary = PaleIchor,              // label of the selected tab
    onSecondary = ClotBlack,
    secondaryContainer = Blood,         // pill behind the selected tab
    onSecondaryContainer = PaleIchor,
    background = ClotBlack,
    onBackground = Bone,
    surface = ClotBlack,
    onSurface = Bone,                   // main text
    surfaceVariant = Scab,
    onSurfaceVariant = DustyRose,       // labels, unselected tabs
    surfaceContainerHighest = Scab,     // cards
    outline = Blood                     // text field border
)

@Composable
fun WeatherAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BloodColorScheme,
        typography = Typography,
        content = content
    )
}
