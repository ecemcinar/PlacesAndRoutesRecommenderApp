package com.wheretogo.placesandroutesrecommenderapp.ui.profile

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn
import com.wheretogo.placesandroutesrecommenderapp.model.Location
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val firestoreRepository: FirebaseFirestoreRepository
): AndroidViewModel(application) {

    private var _getUserFlow = MutableStateFlow<Resource<User?>?>(null)
    val getUserFlow = _getUserFlow.asStateFlow()

    private var _getUserPostListFlow = MutableStateFlow<Resource<List<Post>?>?>(null)
    val getUserPostListFlow = _getUserPostListFlow.asStateFlow()

    private var _getUserCheckInListFlow = MutableStateFlow<Resource<List<CheckIn>?>?>(null)
    val getUserCheckInListFlow = _getUserCheckInListFlow.asStateFlow()

    private var _userPreferencesList = mutableListOf<String>()
    val userPreferencesList get() = _userPreferencesList

    private var _userCheckInCategoryList = mutableListOf<String>()
    val userCheckInCategoryList get() = _userCheckInCategoryList

    private var _getLocationListFlow = MutableStateFlow<Resource<List<Location>>?>(null)
    val getLocationListFlow = _getLocationListFlow.asStateFlow()

    private var _locationList = mutableListOf<Location>()
    val locationList = _locationList

    fun getUser(userId: String) {
        _getUserFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = firestoreRepository.getUser(userId)
            _getUserFlow.value = result
        }
    }

    fun getUserPostList(userId: String) {
        _getUserPostListFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = firestoreRepository.getUserPosts(userId)
            _getUserPostListFlow.value = result
        }
    }

    fun getUserCheckInList(userId: String) {
        _getUserCheckInListFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = firestoreRepository.getUserCheckInList(userId)
            _getUserCheckInListFlow.value = result
        }
    }

    fun getLocationList() {
        _getLocationListFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = firestoreRepository.getLocationList(getMostPreferredCategory())
            _getLocationListFlow.value = result
        }
    }

    fun setLocationList(list: List<Location>) {
        _locationList = list.toMutableList()
    }

    // shown in ui
    fun setUserPreferencesList(list: List<String>) {
        _userPreferencesList = list.toMutableList()
    }

    // previous preferences
    fun setUserCheckInCategoryList(list: List<String>) {
        _userCheckInCategoryList = list.toMutableList()
    }

    private fun getMostPreferredCategory(): String {
        var hashmap = mutableMapOf<String, Int>()
        for (i in 0 until userCheckInCategoryList.size) {
            val category = userCheckInCategoryList[i]
            if (hashmap.containsKey(category)) {
                hashmap.merge(category, 1, Int::plus)
            } else {
                hashmap[category] = 1
            }
        }
        val sortedMap = hashmap.toSortedMap(compareByDescending { it })
        return sortedMap.lastKey()
    }

    fun getRandomLocationFromLocationList(): Location? {
        if (_locationList.isNotEmpty()) {
            val random = (0 until _locationList.size).random()
            return _locationList[random]
        }
        return null
    }
}