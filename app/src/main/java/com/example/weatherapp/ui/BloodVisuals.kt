package com.example.weatherapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.Blood

// The app's card: a normal Material card with a thin blood-red border so it stands out from the dark background
@Composable
fun BloodCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = modifier, border = BorderStroke(1.dp, Blood), content = content)
}
