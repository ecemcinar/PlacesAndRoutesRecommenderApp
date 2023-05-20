package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.android.gms.maps.model.Polyline

data class Route(
    val routeLabels: String,
    val legs: RouteLeg,
    val distanceMeters: Int,
    val duration: String,
    val staticDuration: String,
    val polyline: Polyline,
    val routeToken: String
)