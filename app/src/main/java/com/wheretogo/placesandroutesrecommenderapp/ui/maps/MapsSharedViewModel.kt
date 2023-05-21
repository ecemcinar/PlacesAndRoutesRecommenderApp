package com.wheretogo.placesandroutesrecommenderapp.ui.maps

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.wheretogo.placesandroutesrecommenderapp.repository.placeservice.PlaceRepository
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.checkin.CategoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MapsSharedViewModel @Inject constructor(
    application: Application,
    private val placeRepository: PlaceRepository
): AndroidViewModel(application) {

    private var _searchForLocationResponse = MutableStateFlow<FindAutocompletePredictionsResponse?>(null)
    val searchForLocationResponse = _searchForLocationResponse.asStateFlow()

    private var _fetchPlaceResponse = MutableStateFlow<FetchPlaceResponse?>(null)
    val fetchPlaceResponse = _fetchPlaceResponse.asStateFlow()

    var placeCategoryPredictionList: MutableList<String?> = mutableListOf()

    private var selectedCategory: String? = null

    fun searchForLocation(query: String) {
        viewModelScope.launch {
            placeRepository.searchPlace(query).addOnSuccessListener { response ->
                if (response.autocompletePredictions.isNotEmpty()) {
                    _searchForLocationResponse.value = response
                    val prediction = response.autocompletePredictions[0]
                    placeCategoryPredictionList.clear()
                    for (i in prediction.placeTypes) {
                        placeCategoryPredictionList.add(i.name)
                    }
                    fetchPlace(prediction.placeId)
                }
            }.addOnFailureListener {
                if (it is ApiException) {
                    Log.e(ContentValues.TAG, "Place not found: " + it.statusCode)
                }
            }
        }
    }

    private fun fetchPlace(placeId: String) {
        viewModelScope.launch {
            placeRepository.getPlace(placeId).addOnSuccessListener { response ->
                _fetchPlaceResponse.value = response
            }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e(ContentValues.TAG, "Place not found: " + exception.statusCode)
                }
            }
        }
    }

    fun setSelectedCategory(categoryModel: CategoryModel) {
        selectedCategory = categoryModel.category
    }
}