package com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen

import com.google.maps.android.compose.MapProperties

data class MapState(val properties: MapProperties=MapProperties(),
                    val isFallOutMap: Boolean=false)

