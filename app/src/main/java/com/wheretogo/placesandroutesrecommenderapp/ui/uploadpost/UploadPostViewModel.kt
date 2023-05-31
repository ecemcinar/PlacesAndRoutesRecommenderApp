package com.wheretogo.placesandroutesrecommenderapp.ui.uploadpost

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import com.wheretogo.placesandroutesrecommenderapp.util.Util
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

    private var _getUserFlow = MutableStateFlow<Resource<User?>?>(null)
    val getUserFlow = _getUserFlow.asStateFlow()

    var tempFlow = MutableStateFlow<Resource<String>?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun addPostToFirestore(post: Post) {
        _addPostFlow.value = Resource.Loading
        viewModelScope.launch {
            authRepository.currentUser?.let { user ->
                user.displayName?.let {
                    post.userId = user.uid
                    post.date = Util.getCurrentTime()
                    val result = repository.addPost(it, post)
                    _addPostFlow.value = result
                }
            }
        }
    }

    fun addReco(content: String) {
        tempFlow.value = Resource.Loading
        viewModelScope.launch {
            tempFlow.value = Resource.Loading
            val result = repository.addRecommendation(content)
            tempFlow.value = result
        }
    }

    fun uploadClickEvent() {
        viewModelScope.launch {
            _postButtonClickEvent.send(Unit)
        }
    }

    fun getUser(userId: String) {
        _getUserFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.getUser(userId)
            _getUserFlow.value = result
        }
    }
}