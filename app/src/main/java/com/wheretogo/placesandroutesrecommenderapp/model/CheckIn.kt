package com.wheretogo.placesandroutesrecommenderapp.model

data class CheckIn(
    var userId: String? = null,
    var placeName: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var category: String? = null,
    var date: String? = null
)