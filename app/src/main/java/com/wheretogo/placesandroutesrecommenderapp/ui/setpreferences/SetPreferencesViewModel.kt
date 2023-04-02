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

    private var _userPrefList =  mutableListOf<SetPreferencesModel>()

    private var _locationList: MutableList<String> =
        mutableListOf("SHOPPING",
            "BAKERY",
            "CINEMA",
            "MUSEUM",
            "NATURE",
            "MUSIC",
            "THEATRE",
            "DINING",
            "FITNESS",
            "COFFEE")

    private var _setPrefListFlow = MutableStateFlow<Resource<List<String>>?>(null)
    val setPrefListFlow = _setPrefListFlow.asStateFlow()


    private fun setIconForPrefList(): MutableList<SetPreferencesModel?> {
        val list = mutableListOf<SetPreferencesModel?>()
        for (location in _locationList) {
            val item = SetPreferencesModel()
            item.isSelected = false
            item.prefName = location
            item.prefIcon = when (location) {
                "SHOPPING" -> R.drawable.shopping
                "BAKERY" -> R.drawable.bakery
                "CINEMA" -> R.drawable.cinema
                "MUSEUM" -> R.drawable.museum
                "NATURE" -> R.drawable.landscape
                "MUSIC" -> R.drawable.music
                "THEATRE" -> R.drawable.theatre
                "DINING" -> R.drawable.dining
                "FITNESS" -> R.drawable.dumbbell
                "COFFEE" -> R.drawable.coffee
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

    fun setPrefListToUser() {
        _setPrefListFlow.value = Resource.Loading
        // TODO
    }
}