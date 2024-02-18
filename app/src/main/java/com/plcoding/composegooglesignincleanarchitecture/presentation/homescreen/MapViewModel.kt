import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.MarkerState
import com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen.MapEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
data class AutocompleteResult(
    val address: String,
    val placeId: String,
)

data class MarkerData(
    val latitude: Double,
    val longitude: Double,
    val title: String? = null,
    val snippet: String? = null
)


@HiltViewModel
class MapViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {

    val locationAutofill = mutableStateListOf<AutocompleteResult>()
    private var job: Job? = null

    private val _currentLatLong = MutableStateFlow(LatLng(37.3496, -121.9381))
    val currentLatLong: StateFlow<LatLng>
        get() = _currentLatLong


    private val _mapEvent = MutableLiveData<MapEvent>()
    val mapEvent: LiveData<MapEvent>
        get() = _mapEvent

    fun triggerMapEvent(event: MapEvent) {
        _mapEvent.value = event
        // Update currentLatLong based on event
        if (event is MapEvent.UpdateLocation) {
            updateCurrentLatLong(event.latitude, event.longitude)
            //currentLatLong.value = LatLng(event.latitude, event.longitude)
        }
    }

    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                locationAutofill += response.autocompletePredictions.map {
                    AutocompleteResult(
                        it.getFullText(null).toString(), it.placeId
                    )
                }
            }.addOnFailureListener {
                it.printStackTrace()
                println(it.cause)
                println(it.message)
            }
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            triggerMapEvent(MapEvent.UpdateLocation(place.latLng!!.latitude, place.latLng!!.longitude))
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }

    fun getCurrentLocation(): LatLng {
        return currentLatLong.value
    }
    fun updateCurrentLatLong(lat: Double, long: Double) {
        _currentLatLong.value = LatLng(lat, long)
    }
}
