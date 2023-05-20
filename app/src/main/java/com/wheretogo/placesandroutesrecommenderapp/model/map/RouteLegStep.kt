package com.wheretogo.placesandroutesrecommenderapp.model.map

import android.location.Location

data class RouteLegStep(
    val distanceMeters: Int,
    val staticDuration: String,
    val startLocation: Location,
    val endLocation: Location
)