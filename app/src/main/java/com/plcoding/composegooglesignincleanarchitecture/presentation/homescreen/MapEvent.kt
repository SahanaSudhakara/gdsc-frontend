package com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen

sealed class MapEvent {

    data class UpdateLocation(val latitude: Double, val longitude: Double) : MapEvent()

}

