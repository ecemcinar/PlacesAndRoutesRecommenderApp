package com.wheretogo.placesandroutesrecommenderapp.ui.maps.checkin

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import com.wheretogo.placesandroutesrecommenderapp.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CheckInViewModel @Inject constructor(
    application: Application,
    private var firestoreRepository: FirebaseFirestoreRepository
) : AndroidViewModel(application) {

    private var _addCheckInFlow = MutableStateFlow<Resource<CheckIn>?>(null)
    val addCheckInFlow = _addCheckInFlow.asStateFlow()

    private var selectedCategory: String? = null
    private var selectedPlaceName: String? = null
    private var selectedPlaceLatlng: LatLng? = null

    fun setSelectedCategory(categoryModel: CategoryModel) {
        selectedCategory = categoryModel.category
    }

    fun setSelectedCategoryString(category: String) {
        this.selectedCategory = category
    }

    fun setSelectedName(name: String?) {
        selectedPlaceName = name
    }

    fun setSelectedLatlng(latLng: LatLng?) {
        selectedPlaceLatlng = latLng
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCheckInToFirebase(
        userId: String
    ) {
        _addCheckInFlow.value = Resource.Loading
        val checkIn = CheckIn().apply {
            this.userId = userId
            this.placeName = selectedPlaceName
            this.latitude = selectedPlaceLatlng?.latitude.toString()
            this.longitude = selectedPlaceLatlng?.longitude.toString()
            this.category = selectedCategory
            this.date = Util.getCurrentTime()
        }
        viewModelScope.launch {
            val result = firestoreRepository.addCheckIn(checkIn)
            _addCheckInFlow.value = result
        }
    }
}