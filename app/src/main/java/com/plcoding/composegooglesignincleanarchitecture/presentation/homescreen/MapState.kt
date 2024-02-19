package com.plcoding.composegooglesignincleanarchitecture.presentation.homescreen

import AutocompleteResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties=MapProperties(),
    val isFallOutMap: Boolean=false,
    var searchName:String="",
    var markerState: LatLng=LatLng(37.3496, -121.9381))
  //  var locationAutofill:  MutableList<AutocompleteResult>   = mutableListOf()  )

