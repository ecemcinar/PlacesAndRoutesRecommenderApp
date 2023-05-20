package com.ecemsevvalcinar.easyroute

import android.util.Log
import com.ecemsevvalcinar.easyroute.EasyRoutesDrawer
import com.ecemsevvalcinar.easyroute.model.LegsItem
import com.ecemsevvalcinar.easyroute.model.RoutesItem
import com.ecemsevvalcinar.easyroute.rest.DirectionsRestImp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Extension function to draw the route
 *
 * @param context
 * @param easyRoutesDirections
 * @param routeDrawer
 * @param markersListCallback
 * @param googleMapsLink
 * @param legsCallback
 */
fun GoogleMap.drawRoute(
    easyRoutesDirections: EasyRoutesDirections,
    routeDrawer: EasyRoutesDrawer = EasyRoutesDrawer.Builder(this)
        .pathWidth(12f)
        .build(),
    markersListCallback: ((markers: List<Marker>) -> Unit)? = null,
    googleMapsLink: ((String) -> Unit)? = null,
    legsCallback: ((legs: List<LegsItem?>?) -> Unit)? = null
) {

    CoroutineScope(Dispatchers.Default).launch {
        try {
            val directions = DirectionsRestImp().getDirections(easyRoutesDirections)
            val routes = directions.routes

            withContext(Dispatchers.Main) {
                if (routes.isNullOrEmpty().not()) {

                    routeDrawer.drawPath(directions)

                    if (easyRoutesDirections.showDefaultMarkers) {
                        val markers = this@drawRoute.handleLegsDirections(routes)

                        if (markersListCallback != null) {
                            markersListCallback(markers)
                        }
                    }

                    if (routes?.get(0)?.legs.isNullOrEmpty().not()) {
                        if (legsCallback != null) {
                            legsCallback(routes?.get(0)?.legs)
                        }
                    }

                    if(googleMapsLink != null){
                        googleMapsLink(getGoogleMapsLink(easyRoutesDirections))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("EasyRoutesError", "${e.message}")
        }

    }
}

fun getGoogleMapsLink(easyRoutesDirections: EasyRoutesDirections): String {
    val (
        _,
        originLatLng,
        originPlace,
        destinationLatLng,
        destinationPlace,
        waypointsLatLng,
        waypointsPlaces
    ) = easyRoutesDirections

    var url = "https://www.google.com.mx/maps/dir/"

    if(originLatLng != null){
        url += "${originLatLng.latitude},${originLatLng.longitude}/"
    }

    if(originPlace != null){
        url += "${originPlace}/"
    }

    for (waypoint in waypointsLatLng) {
        waypoint.let {
            url += "${waypoint.latitude},${waypoint.longitude}/"
        }
    }

    for (waypoint in waypointsPlaces) {
        waypoint.let {
            url += "${waypoint}/"
        }
    }

    if(destinationLatLng != null){
        url += "${destinationLatLng.latitude},${destinationLatLng.longitude}"
    }

    if(destinationPlace != null){
        url += "${destinationPlace}"
    }

    return url
}

private fun GoogleMap.handleLegsDirections(
    routes: List<RoutesItem?>?
): ArrayList<Marker> {
    val markers = arrayListOf<Marker>()
    if (routes?.get(0)?.legs.isNullOrEmpty().not()) {
        val legsLastIndex = routes?.get(0)?.legs?.lastIndex

        if (legsLastIndex == null && legsLastIndex == -1) {
            throw Exception("Error not found legs")
        }

        routes?.get(0)?.legs?.get(0)?.let { legsItem ->
            //draw origin marker
            legsItem.startLocation?.let { startLocation ->
                if (startLocation.lat != null && startLocation.lng != null) {
                    val originMarker = this@handleLegsDirections.drawDefaultMarker(
                        LatLng(startLocation.lat, startLocation.lng),
                        title = "Origin"
                    )

                    originMarker?.let { markers.add(it) }
                }
            }
        }

        routes?.get(0)?.legs?.get(legsLastIndex ?: 0)?.let { legsItem ->
            //draw destination marker
            legsItem.endLocation?.let { endLocation ->
                if (endLocation.lat != null && endLocation.lng != null) {
                    val destinationMarker = this@handleLegsDirections.drawDefaultMarker(
                        LatLng(endLocation.lat, endLocation.lng),
                        title = "Destination"
                    )

                    destinationMarker?.let { markers.add(it) }
                }
            }
        }
    }

    return markers
}

fun GoogleMap.drawDefaultMarker(
    location: LatLng,
    title: String
): Marker? {
    val markerOptions = MarkerOptions()
        .position(location)
        .title(title)

    val marker = this.addMarker(
        markerOptions
    )

    marker?.tag = "Route"

    return marker
}