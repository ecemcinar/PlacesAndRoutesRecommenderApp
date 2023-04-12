package com.wheretogo.placesandroutesrecommenderapp.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FeedViewModel @Inject constructor(
    application: Application,
    private val repository: FirebaseFirestoreRepository
) : AndroidViewModel(application) {

    private var _getPostListFlow = MutableStateFlow<Resource<List<Post>>?>(null)
    val getPostListFlow = _getPostListFlow.asStateFlow()

    fun getPostList() {
        _getPostListFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.getPosts()
            _getPostListFlow.value = result
        }
    }
}