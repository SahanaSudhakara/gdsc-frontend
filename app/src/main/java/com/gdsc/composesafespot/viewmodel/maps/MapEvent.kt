package com.gdsc.composesafespot.viewmodel.maps

sealed class MapEvent {
    data class SearchName(val searchName: String):MapEvent()

}

