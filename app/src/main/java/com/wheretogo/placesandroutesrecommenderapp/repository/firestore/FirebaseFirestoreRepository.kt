package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.util.Resource

interface FirebaseFirestoreRepository {

    suspend fun addUser(user: User): Resource<User>
}