package com.wheretogo.placesandroutesrecommenderapp.model

data class User(
    val nameAndSurname: String? = null,
    val email: String? = null,
    val prefList: List<String> = emptyList()
) {
    private val postList: MutableList<Post?>? = null
    private val checkInList: MutableList<CheckIn?>? = null
}