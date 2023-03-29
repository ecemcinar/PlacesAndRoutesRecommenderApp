package com.wheretogo.placesandroutesrecommenderapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SharedAuthViewModel @Inject constructor(
    application: Application,
    private val repository: FirebaseAuthRepository
) : AndroidViewModel(application) {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow = _loginFlow.asStateFlow()

    private val _signUpFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow = _signUpFlow.asStateFlow()

    private var _otherOptionButtonClick = Channel<Unit>()
    val otherOptionButtonClick = _otherOptionButtonClick.receiveAsFlow()

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        repository.currentUser?.let {
            _loginFlow.value = Resource.Success(it)
        }
    }

    fun login(email: String, password: String) {
        _loginFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginFlow.value = result
        }
    }

    fun signup(name: String, email: String, password: String) {
        _signUpFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.signup(name, email, password)
            _signUpFlow.value = result
        }
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signUpFlow.value = null
    }

    fun otherOptionClick() {
        viewModelScope.launch {
            _otherOptionButtonClick.send(Unit)
        }
    }
}