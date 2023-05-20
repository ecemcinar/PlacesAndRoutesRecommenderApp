package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.gson.annotations.SerializedName

data class RouteTravelAdvisory(
    val tollInfo: TollInfo? = null,
    val speedReadingIntervals: List<SpeedReadingInterval?>? = null,
    @SerializedName("fuelConsumptionMicroliters")
    val fuelConsumptionMicroLiters: String? = null
)
