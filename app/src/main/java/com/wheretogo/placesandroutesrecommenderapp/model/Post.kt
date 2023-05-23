package com.wheretogo.placesandroutesrecommenderapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    var username: String? = null,
    var title: String? = null,
    var content: String? = null,
    var userId: String? = null,
    var userProfileImage: String? = null,
    var date: String? = null
): Parcelable