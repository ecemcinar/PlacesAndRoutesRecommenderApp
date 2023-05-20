package com.wheretogo.placesandroutesrecommenderapp.repository.googlemaps

import com.wheretogo.placesandroutesrecommenderapp.model.map.RouteRequest
import com.wheretogo.placesandroutesrecommenderapp.model.map.RouteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleMapsService {

    @POST("distanceMatrix/v2:computeRouteMatrix")
    suspend fun getRoutes(@Body request: RouteRequest): Call<RouteResponse>
}