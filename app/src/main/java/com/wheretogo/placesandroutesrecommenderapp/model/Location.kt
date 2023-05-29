package com.wheretogo.placesandroutesrecommenderapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    var locationName: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var category: String? = null
): Parcelable
