package com.wheretogo.placesandroutesrecommenderapp.repository.storage

import android.net.Uri
import com.wheretogo.placesandroutesrecommenderapp.util.Resource

interface FirebaseStorageRepository {

    suspend fun addUserProfilePhoto(selectedImage: Uri, userId: String): Resource<String>
}