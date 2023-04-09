package com.wheretogo.placesandroutesrecommenderapp.repository.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.wheretogo.placesandroutesrecommenderapp.repository.await
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(
    private var firebaseStorage: FirebaseStorage
): FirebaseStorageRepository {
    override suspend fun addUserProfilePhoto(selectedImage: Uri, userId: String): Resource<String> {
        return try {
            val imageRef = firebaseStorage.reference.child("images").child("$userId.jpg")

            val result = imageRef.putFile(selectedImage).await()
            Resource.Success(imageRef.downloadUrl.await().toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}