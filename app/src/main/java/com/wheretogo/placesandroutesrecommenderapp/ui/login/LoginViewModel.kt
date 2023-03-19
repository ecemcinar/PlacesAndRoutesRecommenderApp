package com.wheretogo.placesandroutesrecommenderapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private var _loginButtonClickEvent = Channel<Unit>()
    val loginButtonClickEvent = _loginButtonClickEvent.receiveAsFlow()

    private var _signUpButtonClickEvent = Channel<Unit>()
    val signUpButtonClickEvent = _signUpButtonClickEvent.receiveAsFlow()

    fun loginButtonClick() {
        viewModelScope.launch {
            _loginButtonClickEvent.send(Unit)
        }
    }

    fun signUpButtonClick() {
        viewModelScope.launch {
            _signUpButtonClickEvent.send(Unit)
        }
    }
}