package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.BAKERY
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.CINEMA
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.COFFEE
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.DINING
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.FITNESS
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.MUSEUM
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.MUSIC
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.NATURE
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.SHOPPING
import com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences.SetPreferencesModel.Companion.THEATRE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetPreferencesViewModel(application: Application): AndroidViewModel(application) {

    private var _prefList = MutableStateFlow<MutableList<SetPreferencesModel?>?>(null)
    val prefList = _prefList.asStateFlow()

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
}