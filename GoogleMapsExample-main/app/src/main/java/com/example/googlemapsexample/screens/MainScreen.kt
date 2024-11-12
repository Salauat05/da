package com.example.googlemapsexample.screens

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.googlemapsexample.utils.LocationUtils
import com.example.googlemapsexample.viewmodel.LocationViewModel

@Composable
fun MainScreen(
    context: Context,
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navHostController: NavHostController
) {
    val pickedLocation by viewModel.pickedLocationData.collectAsState()
    val userLocationData by viewModel.userLocationData.collectAsState()
    val userLocationAddress by viewModel.userLocationAddress.collectAsState()

    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permission ->
            if (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
                && permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                locationUtils.getLocation()
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Current Location",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.Start),
            color = MaterialTheme.colorScheme.primary
        )

        // Отображение координат или адреса
        if (userLocationData != null) {
            Text(
                text = "Lat: ${userLocationData!!.latitude}, Long: ${userLocationData!!.longitude}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Text(
                text = "Fetching location...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Отображение адреса, если доступен
        if (userLocationAddress.isNotEmpty()) {
            Text(
                text = userLocationAddress,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(Modifier.height(30.dp))

        // Кнопка для запроса местоположения
        Button(
            onClick = {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                navHostController.navigate(route = Graph.MAP_SCREEN)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Pick Location")
        }
    }
}