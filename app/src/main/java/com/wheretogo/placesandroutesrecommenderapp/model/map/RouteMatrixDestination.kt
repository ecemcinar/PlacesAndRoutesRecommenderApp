package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.gson.annotations.SerializedName

data class RouteMatrixDestination(
    @SerializedName("waypoint")
    var waypoint: Waypoint? = null
)
