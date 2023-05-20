package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.FallbackReason
import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.FallbackRoutingMode

data class FallbackInfo(
    val routingMode: FallbackRoutingMode,
    val reason: FallbackReason
)