package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.Speed

data class SpeedReadingInterval(
    val speed: Speed? = null,
    val startPolylinePointIndex: Int? = null,
    val endPolylinePointIndex: Int? = null
)
