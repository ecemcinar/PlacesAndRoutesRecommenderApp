package com.wheretogo.placesandroutesrecommenderapp.model

data class User(
    val userId: String? = null,
    val username: String? = null,
    val email: String? = null,
    val prefList: List<String> = emptyList(),
    val userProfileImage: String? = null
) {
    private val postList: MutableList<Post?>? = null
    private val checkInList: MutableList<CheckIn?>? = null
}