package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.util.Resource

interface FirebaseFirestoreRepository {

    suspend fun addUser(user: User): Resource<User>

    suspend fun setUserPrefList(prefList: List<String>, documentId: String): Resource<List<String>>

    suspend fun addPost(userEmail: String, post: Post): Resource<Post>

    suspend fun addUserProfileImageRef(res: String, documentId: String): Resource<String>
}