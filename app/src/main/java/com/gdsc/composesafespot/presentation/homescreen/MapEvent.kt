package com.gdsc.composesafespot.presentation.homescreen

sealed class MapEvent {
    data class SearchName(val searchName: String):MapEvent()

}

