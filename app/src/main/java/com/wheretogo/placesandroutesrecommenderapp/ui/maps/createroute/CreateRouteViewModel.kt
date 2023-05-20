package com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.wheretogo.placesandroutesrecommenderapp.model.map.LatLng
import com.wheretogo.placesandroutesrecommenderapp.model.map.Location
import com.wheretogo.placesandroutesrecommenderapp.model.map.RouteMatrixDestination
import com.wheretogo.placesandroutesrecommenderapp.model.map.RouteMatrixOrigin
import com.wheretogo.placesandroutesrecommenderapp.model.map.RouteRequest
import com.wheretogo.placesandroutesrecommenderapp.model.map.Waypoint
import com.wheretogo.placesandroutesrecommenderapp.repository.googlemaps.GoogleMapsRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.placeservice.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateRouteViewModel @Inject constructor(
    application: Application,
    private val googleMapsRepository: GoogleMapsRepository,
    private val placeRepository: PlaceRepository
): AndroidViewModel(application) {

    private val _locationList = MutableStateFlow<MutableList<LatLng>?>(null)
    val locationList = _locationList.asStateFlow()

    fun addToLocationList(location: LatLng) {
        locationList.value?.add(location)
    }

    fun setLocationListText(prev: String, new: String): String {
        return "$prev $new"
    }


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
}