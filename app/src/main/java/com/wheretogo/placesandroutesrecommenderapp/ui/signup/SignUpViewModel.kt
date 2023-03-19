package com.wheretogo.placesandroutesrecommenderapp.ui.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application): AndroidViewModel(application) {

    private var _continueButtonClickEvent = Channel<Unit>()
    val continueButtonClickEvent = _continueButtonClickEvent.receiveAsFlow()

    private var _loginClickEvent = Channel<Unit>()
    val loginClickEvent = _loginClickEvent.receiveAsFlow()

    fun continueButtonClick() {
        viewModelScope.launch {
            _continueButtonClickEvent.send(Unit)
        }
    }

    fun loginButtonClick() {
        viewModelScope.launch {
            _loginClickEvent.send(Unit)
        }
    }
}