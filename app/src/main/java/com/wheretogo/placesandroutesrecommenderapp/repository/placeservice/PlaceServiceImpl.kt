package com.wheretogo.placesandroutesrecommenderapp.repository.placeservice

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import javax.inject.Inject

class PlaceServiceImpl @Inject constructor(private val placesClient: PlacesClient) : PlaceService {
    override fun findAutocompletePredictions(query: String): Task<FindAutocompletePredictionsResponse> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        return placesClient.findAutocompletePredictions(request)
    }

    override fun fetchPlace(placeId: String): Task<FetchPlaceResponse> {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, fields).build()
        return placesClient.fetchPlace(fetchPlaceRequest)
    }
}