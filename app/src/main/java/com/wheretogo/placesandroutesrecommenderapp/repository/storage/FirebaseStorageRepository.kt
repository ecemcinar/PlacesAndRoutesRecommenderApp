package com.wheretogo.placesandroutesrecommenderapp.repository.storage

import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.util.Resource

interface FirebaseStorageRepository {

    suspend fun addUserProfilePhoto(user: User): Resource<User>
}