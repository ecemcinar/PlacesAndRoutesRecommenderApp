package com.wheretogo.placesandroutesrecommenderapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn
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
}