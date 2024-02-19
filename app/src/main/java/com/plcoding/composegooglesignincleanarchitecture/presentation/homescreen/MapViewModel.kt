import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen.MapEvent
import com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AutocompleteResult(
    val address: String,
    val placeId: String,
)


@HiltViewModel
class MapViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {
    var mapState= mutableStateOf(MapState())

    var updateLatLng = mutableStateOf(false)
    var newLatLng=LatLng(37.3496, -121.9381)

    val locationAutofill = mutableStateListOf<AutocompleteResult>()
    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.SearchName -> {
                mapState.value =
                    mapState.value.copy(searchName = event.searchName)                //mapState.value = mapState.value.copy(searchName = event.searchName, locationAutofill = searchPlaces(event.searchName,mapState.value.locationAutofill ))
                // Call searchPlaces method to search for places with the given query
                searchPlaces(event.searchName, locationAutofill)
                //    mapState.value = mapState.value.copy(searchName = event.searchName, locationAutofill = mapState.value.locationAutofill
            }

            else -> {}
        }
    }
    private var job: Job? = null

    fun searchPlaces(query: String, locationAutofill: MutableList<AutocompleteResult>) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                locationAutofill.addAll(response.autocompletePredictions.map {
                    AutocompleteResult(
                        it.getFullText(null).toString(), it.placeId
                    )
                })
                updateLatLng.value=true
                // No need to return locationAutofill as it's already updated in-place
            }.addOnFailureListener { exception ->
                // Handle failure
                exception.printStackTrace()
                println(exception.cause)
                println(exception.message)
            }
        }
    }



    fun getCoordinates(result: AutocompleteResult, onResult: (LatLng?) -> Unit) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            if (place != null) {
                val latLng = place.latLng
                onResult(latLng)
            } else {
                // Place is null, handle the failure
                onResult(null)
            }
        }.addOnFailureListener { exception ->
            // Handle failure
            exception.printStackTrace()
            onResult(null)
        }
    }



}
