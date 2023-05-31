package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SetPreferencesViewModel @Inject constructor(
    application: Application,
    private val repository: FirebaseFirestoreRepository
) : AndroidViewModel(application) {

    private var _prefList = MutableStateFlow<MutableList<SetPreferencesModel?>?>(null)
    val prefList = _prefList.asStateFlow()

    private var _userPrefList =  mutableListOf<SetPreferencesModel?>()

    private var _locationList: MutableList<String> =
        mutableListOf("SHOPPING_MALL",
            "BAKERY",
            "CINEMA_THEATRE",
            "MUSEUM",
            "PARK",
            "MUSIC",
            "FITNESS",
            "CAFE",
            "FOOD",
            "HISTORICAL_PLACE",
            "ZOO")

    private var _setPrefListFlow = MutableStateFlow<Resource<List<String>>?>(null)
    val setPrefListFlow = _setPrefListFlow.asStateFlow()


    private fun setIconForPrefList(): MutableList<SetPreferencesModel?> {
        val list = mutableListOf<SetPreferencesModel?>()
        for (location in _locationList) {
            val item = SetPreferencesModel()
            item.isSelected = false
            item.prefName = location
            item.prefIcon = when (location) {
                "SHOPPING_MALL" -> R.drawable.shopping
                "BAKERY" -> R.drawable.bakery
                "CINEMA_THEATRE" -> R.drawable.theatre
                "MUSEUM" -> R.drawable.museum
                "PARK" -> R.drawable.landscape
                "MUSIC" -> R.drawable.music
                "FOOD" -> R.drawable.dining
                "FITNESS" -> R.drawable.dumbbell
                "CAFE" -> R.drawable.coffee
                "HISTORICAL_PLACE" -> R.drawable.castle
                "ZOO" -> R.drawable.zoo
                else -> null
            }
            list.add(item)
        }
        return list
    }

    fun createPrefList() {
        viewModelScope.launch(Dispatchers.IO) {
            _prefList.emit(
                setIconForPrefList()
            )
        }
    }

    fun setPrefList(item: SetPreferencesModel) {
        if (item.isSelected == true) {
            _userPrefList.add(item)
        } else {
            _userPrefList.remove(item)
        }
    }

    fun setPrefListToUser(documentId: String) {
        _setPrefListFlow.value = Resource.Loading
        viewModelScope.launch {
            val result = repository.setUserPrefList(getLocationFromPrefList(), documentId)
            _setPrefListFlow.value = result
        }
    }

    private fun getLocationFromPrefList(): List<String> {
        val list = mutableListOf<String>()
        for (element in _userPrefList) {
            element?.let { model ->
                model.prefName?.let {
                    list.add(it)
                }
            }
        }
        return list
    }
}