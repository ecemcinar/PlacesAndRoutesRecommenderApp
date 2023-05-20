package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.wheretogo.placesandroutesrecommenderapp.model.map.LatLng

data class Location(
    val latLng: LatLng? = null,
    val heading: Int? = 0
)
