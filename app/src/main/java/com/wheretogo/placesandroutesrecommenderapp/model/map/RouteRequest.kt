package com.wheretogo.placesandroutesrecommenderapp.model.map

import com.google.gson.annotations.SerializedName
import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.RouteTravelMode
import com.wheretogo.placesandroutesrecommenderapp.model.map.enums.RoutingPreference

data class RouteRequest(
    @SerializedName("origins")
    var origins: List<RouteMatrixOrigin?>? = null,
    @SerializedName("destinations")
    var destinations: List<RouteMatrixDestination?>? = null,
    // istege bagli asagidakiler
    @SerializedName("travelMode")
    val travelMode: RouteTravelMode? = null,
    @SerializedName("routingPreference")
    val routingPreference: RoutingPreference? = RoutingPreference.ROUTING_PREFERENCE_UNSPECIFIED, // istege bagli
    @SerializedName("departureTime")
    val departureTime: String? = null,
    val languageCode: String? = null

)