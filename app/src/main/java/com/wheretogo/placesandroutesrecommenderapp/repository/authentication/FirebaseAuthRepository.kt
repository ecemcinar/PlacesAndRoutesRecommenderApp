package com.wheretogo.placesandroutesrecommenderapp.repository.authentication

import com.google.firebase.auth.FirebaseUser
import com.wheretogo.placesandroutesrecommenderapp.util.Resource

interface FirebaseAuthRepository {

    val currentUser: FirebaseUser?

    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    fun logout()
}