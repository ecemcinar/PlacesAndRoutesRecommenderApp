package com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.libraries.places.api.model.Place
import com.wheretogo.placesandroutesrecommenderapp.model.Location
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