package com.wheretogo.placesandroutesrecommenderapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recommendation(
    var title: String? = null,
    var content: String? = null,
    var image: String? = null,
    var placeList: List<Location?>? = null
): Parcelable
