package estg.ipp.pt.friendly.ui.Maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(latitude:Double,longitude:Double){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val permission_given = remember {
            mutableStateOf(0)
        }
        val ctx = LocalContext.current
        if (ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permission_given.value = 2
        }
        val permissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    permission_given.value += 1
                }
            }
        LaunchedEffect(key1 = "Permission") {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        Column {
            if (permission_given.value == 2) {
                LocationUpdates()
            }
            var mapProperties by remember {
                mutableStateOf(
                    MapProperties(maxZoomPreference = 17f, minZoomPreference = 10f)
                )
            }

            var mapUiSettings by remember {
                mutableStateOf(
                    MapUiSettings(mapToolbarEnabled = false)
                )
            }

            Box(Modifier.fillMaxSize()) {
                val recintoPosition =
                    com.google.android.gms.maps.model.LatLng(latitude, longitude)

                val cameraPositionState = rememberCameraPositionState{
                    position = CameraPosition.fromLatLngZoom(recintoPosition,14f)
                }
                GoogleMap(
                    properties = mapProperties, uiSettings = mapUiSettings,
                    cameraPositionState = cameraPositionState
                ) {
                    val context = LocalContext.current
                    MarkerInfoWindow (
                        state = rememberMarkerState(position = recintoPosition),

                    )

                }
            }

        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun LocationUpdates() {
    val text = remember { mutableStateOf("") }
    val ctx = LocalContext.current
    DisposableEffect(Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx)

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
        }.addOnFailureListener {
            text.value = "Unable to get locations"
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000,
        ).setMinUpdateIntervalMillis(1000).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locations: LocationResult) {
                for (location in locations.locations) {
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
        onDispose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
    Text(text = text.value)
}






