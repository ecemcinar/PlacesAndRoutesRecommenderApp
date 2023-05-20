package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.gson.annotations.SerializedName
import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.TollPass

data class RouteModifiers(
    @SerializedName("avoidTolls")
    val avoidTolls: Boolean? = null,
    @SerializedName("avoidHighways")
    val avoidHighways: Boolean? = null,
    @SerializedName("avoidFerries")
    val avoidFerries: Boolean? = null,
    @SerializedName("avoidIndoor")
    val avoidIndoor: Boolean? = null,
    @SerializedName("vehicleInfo")
    val vehicleInfo: VehicleInfo? = null,
    @SerializedName("tollPasses")
    val tollPasses: TollPass? = null
)
