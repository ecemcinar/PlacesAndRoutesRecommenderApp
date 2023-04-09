package com.wheretogo.placesandroutesrecommenderapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.model.User
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
class SignUpViewModel @Inject constructor(
    application: Application,
    private val repository: FirebaseFirestoreRepository) :
    AndroidViewModel(application) {

    private var _loginClickEvent = Channel<Unit>()
    val loginClickEvent = _loginClickEvent.receiveAsFlow()

    private var _addUserFlow = MutableStateFlow<Resource<User>?>(null)
    val addUserFlow = _addUserFlow.asStateFlow()

    fun loginButtonClick() {
        viewModelScope.launch {
            _loginClickEvent.send(Unit)
        }
    }

    fun addUserToFirestore(userId: String, email: String, nameAndSurname: String) {
        _addUserFlow.value = Resource.Loading
        val user = User(
            userId = userId,
            nameAndSurname = nameAndSurname,
            email = email)
        viewModelScope.launch {
            val result = repository.addUser(user)
            _addUserFlow.value = result
        }
    }
}