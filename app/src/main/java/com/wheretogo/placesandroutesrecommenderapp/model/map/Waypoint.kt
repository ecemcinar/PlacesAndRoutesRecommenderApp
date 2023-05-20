package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.gson.annotations.SerializedName

data class Waypoint(
    @SerializedName("via")
    val via: Boolean? = false,
    @SerializedName("vehicleStopover")
    val vehicleStopover: Boolean? = false,
    @SerializedName("sideOfRoad")
    val sideOfRoad: Boolean? = false,
    val location: Location? = null,
    @SerializedName("placeId")
    val placeId: String? = null,
    val address: String? = null
)