package com.wheretogo.placesandroutesrecommenderapp.repository.placeservice

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

interface PlaceService {
    fun findAutocompletePredictions(query: String): Task<FindAutocompletePredictionsResponse>
    fun fetchPlace(placeId: String): Task<FetchPlaceResponse>
}