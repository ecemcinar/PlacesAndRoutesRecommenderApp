package com.wheretogo.placesandroutesrecommenderapp.repository.placeservice

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeService: PlaceService) {
    fun searchPlace(query: String): Task<FindAutocompletePredictionsResponse> {
        return placeService.findAutocompletePredictions(query)
    }

    fun getPlace(placeId: String): Task<FetchPlaceResponse> {
        return placeService.fetchPlace(placeId)
    }
}