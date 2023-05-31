package com.wheretogo.placesandroutesrecommenderapp.ui.routerecommendation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.Location
import com.wheretogo.placesandroutesrecommenderapp.model.Recommendation
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RouteRecommendationViewModel @Inject constructor(
    application: Application,
    private val repository: FirebaseFirestoreRepository
): AndroidViewModel(application) {

    private var _getRecommendationFlow = MutableStateFlow<Resource<Recommendation>?>(null)
    val getRecommendationFlow = _getRecommendationFlow.asStateFlow()

    private var _locationList = mutableListOf<Location?>()
    val locationList get() = _locationList

    fun getRecommendation(docId: String) {
        _getRecommendationFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.getRecommendationById(docId)
            _getRecommendationFlow.value = result
        }
    }

    fun setPlaceList(list: List<Location?>) {
        _locationList = list.toMutableList()
    }
}