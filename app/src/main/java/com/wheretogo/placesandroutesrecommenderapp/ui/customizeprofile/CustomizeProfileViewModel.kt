package com.wheretogo.placesandroutesrecommenderapp.ui.customizeprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CustomizeProfileViewModel(application: Application): AndroidViewModel(application) {

    private var _uploadClickEvent = Channel<Unit>()
    val uploadClickEvent = _uploadClickEvent.receiveAsFlow()

    fun uploadButtonClick() {
        viewModelScope.launch {
            _uploadClickEvent.send(Unit)
        }
    }
}