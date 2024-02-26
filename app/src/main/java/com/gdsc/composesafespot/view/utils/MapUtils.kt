package com.gdsc.composesafespot.view.utils

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.gdsc.composesafespot.R
import com.gdsc.composesafespot.model.data.CrimeStatus
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import org.json.JSONArray
import org.json.JSONException

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }
// Function to read JSON data from the assets directory
// Function to read JSON data from assets directory
fun readJsonFromAssets(context: Context, fileName: String): String? {
    return try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Function to parse JSON data and extract LatLng objects
fun parseJsonData(jsonString: CrimeStatus): List<LatLng> {
    val latLngList = mutableListOf<LatLng>()

    try {
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val latitude = jsonObject.getDouble("lat")
            val longitude = jsonObject.getDouble("lon")
            val latLng = LatLng(latitude, longitude)
            latLngList.add(latLng)
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return latLngList
}
fun parseCrimeStatusList(crimeStatusList: MutableState<List<CrimeStatus>>): List<LatLng> {
    val latLngList = mutableListOf<LatLng>()

    try {
        for (crimeStatus in crimeStatusList.value) {
            val latitude = crimeStatus.latitude.toDouble()
            val longitude = crimeStatus.longitude.toDouble()
            val latLng = LatLng(latitude, longitude)
            latLngList.add(latLng)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return latLngList
}
fun convertDataToLatLngList(data: List<CrimeStatus>): List<LatLng> {
    val latLngList = mutableListOf<LatLng>()
    for (crimeStatus in data) {
        val latitude = crimeStatus.latitude.toDouble()
        val longitude = crimeStatus.longitude.toDouble()
        val latLng = LatLng(latitude, longitude)
        latLngList.add(latLng)
    }
    return latLngList
}

fun addHeatmapLayerToMap(
    googleMap: GoogleMap,
    latLngList: List<LatLng>,
    crimeTypes: List<String>
) {
    // Create a map to hold the intensity for each crime type
    val intensityMap = mutableMapOf<String, Double>()

    // Initialize intensity for each crime type
    for (crimeType in crimeTypes) {
        intensityMap[crimeType] = 0.0
    }

    // Calculate intensity for each crime type based on frequency
    for (latLng in latLngList) {
        // Assign a weight based on the intensity of the crime
        val weight = 1.0 // You can adjust this value based on your data
        // Update intensity for the corresponding crime type
        intensityMap[getCrimeType()] = (intensityMap[getCrimeType()] ?: 0.0) + weight
    }

    // Create a heatmap data set
    val heatmapData = mutableListOf<WeightedLatLng>()

    // Add latitude and longitude of areas with intensity values
    for (latLng in latLngList) {
        // Get the intensity for the crime type of the current location
        val intensity = intensityMap[getCrimeType()] ?: 0.0
        heatmapData.add(
            WeightedLatLng(
                latLng,
                intensity
            )
        )
    }

    // Define the color gradient for the heatmap
    val gradientColors = intArrayOf(
        0xFF008000.toInt(), // Green for low intensity
        0xFFFFFF00.toInt(), // Yellow for high intensity
        0xFFFF0000.toInt()  // Red for very high intensity
    )


    val gradientStartPoints = floatArrayOf(0.1f, 0.5f, 1.0f)
    val gradient = Gradient(gradientColors, gradientStartPoints)

    // Create a heatmap layer with the heatmap data set
    val heatmapProvider = HeatmapTileProvider.Builder()
        .weightedData(heatmapData)
        .radius(50) // optional, in pixels, can be anything between 20 and 50
        .maxIntensity(1000.0) // set the maximum intensity
        .gradient(gradient)
        .build()

    // Add the heatmap layer to the map
    googleMap.addTileOverlay(
        TileOverlayOptions().tileProvider(
            heatmapProvider
        )
    )
}

// Function to get the type of crime based on latitude and longitude
fun getCrimeType(): String {
    // Implement logic to determine the type of crime based on latitude and longitude
    // You can use geocoding, reverse geocoding, or any other method to get this information
    // For the sake of example, let's assume the type of crime is determined randomly
    val crimeTypes = listOf("Assault", "Burglary", "Drug Offense", "Disorderly Conduct")
    return crimeTypes.random()
}


