package com.wheretogo.placesandroutesrecommenderapp.repository.googlemaps

import com.wheretogo.placesandroutesrecommenderapp.model.map.RouteRequest
import javax.inject.Inject

class GoogleMapsRepository @Inject constructor(
    private val apiService: GoogleMapsService
) {
    suspend fun getRoutes(request: RouteRequest) = apiService.getRoutes(request)
}