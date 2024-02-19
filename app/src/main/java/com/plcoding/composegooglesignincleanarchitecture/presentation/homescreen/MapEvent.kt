package com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen

sealed class MapEvent {
    data class SearchName(val searchName: String):MapEvent()

}

