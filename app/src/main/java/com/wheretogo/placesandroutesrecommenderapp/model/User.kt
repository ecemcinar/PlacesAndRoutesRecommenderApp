package com.wheretogo.placesandroutesrecommenderapp.model

data class User(
    private val userNameSurname: String? = null
) {
    private val postList: MutableList<Post?>? = null
    private val checkInList: MutableList<CheckIn?>? = null
}