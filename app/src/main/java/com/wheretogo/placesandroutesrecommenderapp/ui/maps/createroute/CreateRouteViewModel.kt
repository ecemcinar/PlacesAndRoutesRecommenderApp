package com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ecemsevvalcinar.easyroute.EasyRoutesDirections
import com.ecemsevvalcinar.easyroute.enums.TransportationMode
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.wheretogo.placesandroutesrecommenderapp.model.Location
import com.wheretogo.placesandroutesrecommenderapp.util.MapUtility.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CreateRouteViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {

    private val _locationList = MutableStateFlow<MutableList<Place>?>(mutableListOf())
    val locationList = _locationList.asStateFlow()

    private var _recommendedLocationList = mutableListOf<Location>()
    val recommendedLocationList get() = _recommendedLocationList

    fun addToLocationList(location: Place) {
        _locationList.value?.add(location)
    }

    fun clearLocationList() {
        _locationList.value?.clear()
    }

    fun setLocationList(list: List<Location>) {
        _recommendedLocationList = list.toMutableList()
    }

    fun createEasyRouteDirectionsFromUserSearch(): EasyRoutesDirections {
        val placeDirection = EasyRoutesDirections().apply {
            apiKey = API_KEY
            showDefaultMarkers = false
            transportationMode = TransportationMode.WALKING
        }
        var placeList = mutableListOf<Place>()
        _locationList.value?.let {
            placeList = it
        }
        val waypointList = mutableListOf<LatLng>()
        for (i in 1..placeList.size-2) {
            placeList[i].latLng?.let {
                waypointList.add(it)
            }
        }
        placeDirection.apply {
            originPlace = placeList.getOrNull(0)?.name.toString()
            destinationPlace = placeList.getOrNull(placeList.size-1)?.name.toString()
            waypointsLatLng = ArrayList(waypointList)
        }

        return placeDirection
    }

    fun createEasyRouteDirectionsFromRecommendationList(): EasyRoutesDirections {
        val placeDirection = EasyRoutesDirections().apply {
            originPlace = _recommendedLocationList.getOrNull(0)?.locationName
            destinationPlace = _recommendedLocationList.getOrNull(_recommendedLocationList.size-1)?.locationName
            apiKey = API_KEY
            showDefaultMarkers = false
            transportationMode = TransportationMode.WALKING
        }
        val waypointList = mutableListOf<LatLng>()
        for (i in 1.._recommendedLocationList.size-2) {
            var latitude = .0
            var longitude = .0
            recommendedLocationList[i].latitude?.let {
                latitude = it.toDouble()
            }
            recommendedLocationList[i].longitude?.let {
                longitude = it.toDouble()
            }
            waypointList.add(LatLng(latitude, longitude))
        }
        placeDirection.waypointsLatLng = ArrayList(waypointList)

        return placeDirection
    }

    /*
    fun getRoute() {
        val routeRequest = RouteRequest().apply {
            origins = listOf(
                RouteMatrixOrigin(
                    waypoint = Waypoint(
                        location = Location(
                            latLng = LatLng(
                                longitude = locationList.value?.get(0)?.longitude,
                                latitude = locationList.value?.get(0)?.latitude
                            )
                        )
                    )
                ))
            destinations = listOf(
                RouteMatrixDestination(
                    waypoint = Waypoint(
                        location = Location(
                            LatLng(
                                latitude = locationList.value?.get(1)?.latitude,
                                longitude = locationList.value?.get(1)?.longitude
                            )
                        )
                    )
                )
            )
        }

        viewModelScope.launch {
            googleMapsRepository.getRoutes(routeRequest)
        }
    }
     */

}