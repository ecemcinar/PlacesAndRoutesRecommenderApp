package com.ecemsevvalcinar.easyroute

import com.ecemsevvalcinar.easyroute.enums.TransportationMode
import com.google.android.gms.maps.model.LatLng

data class EasyRoutesDirections(
    var apiKey: String? = null,
    var originLatLng: LatLng? = null,
    var originPlace: String? = null,

    var destinationLatLng: LatLng? = null,
    var destinationPlace: String? = null,

    var waypointsLatLng: ArrayList<LatLng> = arrayListOf(),
    var waypointsPlaces: ArrayList<String> = arrayListOf(),
    var transportationMode: TransportationMode = TransportationMode.DRIVING,

    var showDefaultMarkers: Boolean = true,
)