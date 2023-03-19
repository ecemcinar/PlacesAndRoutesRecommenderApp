package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

class SetPreferencesModel {

    var prefName: String? = null
    var prefIcon: Int? = null
    var isSelected: Boolean? = false

    companion object {
        const val SHOPPING = "1"
        const val BAKERY = "2"
        const val CINEMA = "3"
        const val MUSEUM = "4"
        const val NATURE = "5"
        const val MUSIC = "6"
        const val THEATRE = "7"
        const val DINING = "8"
        const val FITNESS = "9"
        const val COFFEE = "10"
    }
}