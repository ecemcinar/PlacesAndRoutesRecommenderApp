package com.wheretogo.placesandroutesrecommenderapp.model

data class User(
    val nameAndSurname: String? = null,
    val email: String? = null
) {
    private val postList: MutableList<Post?>? = null
    private val checkInList: MutableList<CheckIn?>? = null
}