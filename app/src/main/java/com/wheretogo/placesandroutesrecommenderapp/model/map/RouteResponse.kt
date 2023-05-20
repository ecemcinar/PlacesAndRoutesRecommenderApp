package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.android.gms.common.api.Status
import com.google.gson.annotations.SerializedName
import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.RouteMatrixElementCondition

data class RouteResponse(
    @SerializedName("status")
    val status: Status? = null,
    @SerializedName("condition")
    val condition: RouteMatrixElementCondition? = null,
    @SerializedName("distanceMeters")
    val distanceMeters: Int? = null,
    @SerializedName("duration")
    val duration: String? = null,
    @SerializedName("staticDuration")
    val staticDuration: String? = null,
    @SerializedName("travelAdvisory")
    val travelAdvisory: RouteTravelAdvisory? = null,
    val fallbackInfo: FallbackInfo? = null,
    val originIndex: Int? = null,
    val destinationIndex: Int? = null
)
