package com.wheretogo.placesandroutesrecommenderapp.repository.storage

import com.google.firebase.storage.FirebaseStorage
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(
    private var firebaseStorage: FirebaseStorage
): FirebaseStorageRepository {
    override suspend fun addUserProfilePhoto(user: User): Resource<User> {
        TODO("Not yet implemented")
    }
}