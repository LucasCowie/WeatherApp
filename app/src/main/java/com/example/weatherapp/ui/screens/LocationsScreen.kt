package com.example.weatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.BloodCard

// "My Location", the user's "Saved Locations", and a search box to find and save more cities.
// onMyLocation: user chose "My Location".  onCitySelected: user tapped a city (passed as "id:123").
@Composable
fun LocationsScreen(
    viewModel: MainViewModel,
    onMyLocation: () -> Unit,
    onCitySelected: (String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionTitle("My Location") }
        item {
            LocationRow(
                title = viewModel.myLocationName ?: "Use this device's location",
                subtitle = "Where this device is right now",
                icon = R.drawable.ic_my_location,
                onClick = onMyLocation
            )
        }

        item { SectionTitle("Saved Locations") }
        if (viewModel.savedLocations.isEmpty()) {
            item { Text("Search for a city below and tap + to save it.") }
        }
        items(viewModel.savedLocations) { city ->
            LocationRow(
                title = city.name,
                subtitle = "${city.region}, ${city.country}",
                icon = R.drawable.ic_location,
                onClick = { onCitySelected("id:${city.id}") },
                actionIcon = R.drawable.ic_delete,
                actionLabel = "Remove ${city.name} from saved locations",
                onAction = { viewModel.removeSavedLocation(city) }
            )
        }

        item { SectionTitle("Add a City") }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search for a city") },
                singleLine = true,
                // Search from the keyboard's search key or the magnifier button
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { viewModel.searchCities(query) }),
                trailingIcon = {
                    IconButton(onClick = { viewModel.searchCities(query) }) {
                        Icon(painterResource(R.drawable.ic_search), contentDescription = "Search")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        viewModel.searchError?.let { message ->
            item { Text(message) }
        }
        items(viewModel.searchResults) { city ->
            LocationRow(
                title = city.name,
                subtitle = "${city.region}, ${city.country}",
                icon = R.drawable.ic_location,
                onClick = { onCitySelected("id:${city.id}") },
                actionIcon = R.drawable.ic_add,
                actionLabel = "Save ${city.name}",
                onAction = { viewModel.addSavedLocation(city) }
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

// One tappable location. Optionally has a button on the right (add or remove).
@Composable
private fun LocationRow(
    title: String,
    subtitle: String,
    icon: Int,
    onClick: () -> Unit,
    actionIcon: Int? = null,
    actionLabel: String? = null,
    onAction: () -> Unit = {}
) {
    BloodCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape) // so the tap ripple has rounded corners too
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(icon), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (actionIcon != null) {
                IconButton(onClick = onAction) {
                    Icon(painterResource(actionIcon), contentDescription = actionLabel)
                }
            }
        }
    }
}
