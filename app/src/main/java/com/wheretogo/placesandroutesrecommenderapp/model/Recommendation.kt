package com.wheretogo.placesandroutesrecommenderapp.model

data class Recommendation(
    var title: String? = null,
    var content: String? = null,
    var image: String? = null,
    var placeList: List<String?>? = null
)
