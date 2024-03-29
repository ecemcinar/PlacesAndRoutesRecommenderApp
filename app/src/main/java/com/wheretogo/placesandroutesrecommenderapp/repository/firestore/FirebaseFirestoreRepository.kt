package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn
import com.wheretogo.placesandroutesrecommenderapp.model.Location
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.model.Recommendation
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.util.Resource

interface FirebaseFirestoreRepository {

    suspend fun addUser(user: User): Resource<User>

    suspend fun setUserPrefList(prefList: List<String>, documentId: String): Resource<List<String>>

    suspend fun addPost(username: String, post: Post): Resource<Post>

    suspend fun addUserProfileImageRef(res: String, documentId: String): Resource<String>

    suspend fun getUser(documentId: String): Resource<User?>

    suspend fun getPosts(): Resource<List<Post>>

    suspend fun addCheckIn(checkIn: CheckIn): Resource<CheckIn>

    suspend fun getUserPosts(userId: String): Resource<List<Post>>

    suspend fun getUserCheckInList(userId: String): Resource<List<CheckIn>>

    suspend fun getRecommendationList(): Resource<List<Recommendation>>

    suspend fun addRecommendation(content: String): Resource<String>

    suspend fun getRecommendationById(docId: String): Resource<Recommendation>

    suspend fun getLocationList(category: String): Resource<List<Location>>
}