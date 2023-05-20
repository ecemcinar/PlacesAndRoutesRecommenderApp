package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.gson.annotations.SerializedName

data class RouteMatrixOrigin(
    @SerializedName("waypoint")
    var waypoint: Waypoint? = null,
    @SerializedName("routeModifiers")
    var routeModifiers: RouteModifiers? = null
)
