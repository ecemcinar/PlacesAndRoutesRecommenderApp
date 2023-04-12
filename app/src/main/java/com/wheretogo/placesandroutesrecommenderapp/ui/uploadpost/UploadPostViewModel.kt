package com.wheretogo.placesandroutesrecommenderapp.ui.uploadpost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UploadPostViewModel @Inject constructor(
    application: Application,
    private val repository: FirebaseFirestoreRepository,
    private val authRepository: FirebaseAuthRepository) :
    AndroidViewModel(application) {

    private var _addPostFlow = MutableStateFlow<Resource<Post>?>(null)
    val addPostFlow = _addPostFlow.asStateFlow()

    private var _postButtonClickEvent = Channel<Unit>()
    val postButtonClickEvent = _postButtonClickEvent.receiveAsFlow()

    fun addPostToFirestore(post: Post) {
        _addPostFlow.value = Resource.Loading
        viewModelScope.launch {
            authRepository.currentUser?.let { user ->
                user.displayName?.let {
                    val result = repository.addPost(it, post)
                    _addPostFlow.value = result
                }
            }
        }
    }

    fun uploadClickEvent() {
        viewModelScope.launch {
            _postButtonClickEvent.send(Unit)
        }
    }

}