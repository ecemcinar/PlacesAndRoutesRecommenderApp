package com.wheretogo.placesandroutesrecommenderapp.model.map

import android.location.Location
import com.google.android.gms.maps.model.Polyline

data class RouteLeg(
    val distanceMeters: Int,
    val duration: Int,
    val staticDuration: Int,
    val polyline: Polyline,
    val startLocation: Location,
    val endLocation: Location,
    val steps: RouteLegStep
)