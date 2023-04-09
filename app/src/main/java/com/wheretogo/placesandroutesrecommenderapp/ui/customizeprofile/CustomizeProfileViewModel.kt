package com.wheretogo.placesandroutesrecommenderapp.ui.customizeprofile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.storage.FirebaseStorageRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CustomizeProfileViewModel @Inject constructor(
    application: Application,
    private val storageRepository: FirebaseStorageRepository,
    private val firestoreRepository: FirebaseFirestoreRepository
) : AndroidViewModel(application) {

    private var _uploadClickEvent = Channel<Unit>()
    val uploadClickEvent = _uploadClickEvent.receiveAsFlow()

    private var _imageClickEvent = Channel<Unit>()
    val imageClickEvent = _imageClickEvent.receiveAsFlow()

    private var _uploadImageFlow = MutableStateFlow<Resource<String>?>(null)
    val uploadImageFlow = _uploadImageFlow.asStateFlow()

    private var _addFirestoreFlow = MutableStateFlow<Resource<String>?>(null)
    val addFirestoreFlow = _addFirestoreFlow.asStateFlow()

    fun uploadButtonClick() {
        viewModelScope.launch {
            _uploadClickEvent.send(Unit)
        }
    }

    fun imageClick() {
        viewModelScope.launch {
            _imageClickEvent.send(Unit)
        }
    }

    fun uploadImageToStorage(selectedImage: Uri, userId: String) {
        _uploadImageFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = storageRepository.addUserProfilePhoto(selectedImage, userId)
            _uploadImageFlow.value = result
        }
    }

    fun addImageRefToFirestore(res: String, userId: String) {
        _addFirestoreFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = firestoreRepository.addUserProfileImageRef(res, userId)
            _addFirestoreFlow.value = result
        }
    }
}