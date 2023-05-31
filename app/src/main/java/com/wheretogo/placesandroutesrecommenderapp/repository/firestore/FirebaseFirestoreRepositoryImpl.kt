package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn
import com.wheretogo.placesandroutesrecommenderapp.model.Location
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.model.Recommendation
import com.wheretogo.placesandroutesrecommenderapp.model.User
import com.wheretogo.placesandroutesrecommenderapp.repository.await
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import javax.inject.Inject

class FirebaseFirestoreRepositoryImpl  @Inject constructor(
    private var firebaseFirestore: FirebaseFirestore
) : FirebaseFirestoreRepository {

    override suspend fun addUser(user: User): Resource<User> {
        return try {
            val userMap = hashMapOf(
                "userId" to user.userId,
                "username" to user.username,
                "email" to user.email
            )

            firebaseFirestore.collection("users").document(user.userId!!)
                .set(userMap).await()
            Resource.Success(user)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun setUserPrefList(prefList: List<String>, documentId: String): Resource<List<String>> {
        return try {
            val userMap = hashMapOf(
                "prefList" to prefList,
            )
            firebaseFirestore.collection("users").document(documentId)
                .update(userMap as Map<String, Any>).await()
            Resource.Success(prefList)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addPost(username: String, post: Post): Resource<Post> {
        return try {
            val postMap = hashMapOf(
                "username" to username,
                "title" to post.title,
                "content" to post.content,
                "userId" to post.userId,
                "userProfileImage" to post.userProfileImage,
                "date" to post.date,
                "time" to Timestamp.now()
            )

            firebaseFirestore.collection("posts")
                .add(postMap).await()
            Resource.Success(post)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addUserProfileImageRef(res: String, documentId: String): Resource<String> {
        return try {
            val data = hashMapOf(
                "userProfileImage" to res
            )

            firebaseFirestore.collection("users").document(documentId)
                .update(data as Map<String, Any>).await()
            Resource.Success(res)
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUser(documentId: String): Resource<User?> {
        var user: User? = User()
        return try {
            val result = firebaseFirestore.collection("users").document(documentId)
                .get().await()
            if (result.exists()) {
                user = result.toObject<User>()
            }
            Resource.Success(user)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getPosts(): Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        return try {
            val result = firebaseFirestore.collection("posts").orderBy("time", Query.Direction.DESCENDING)
                .get().await()
            for (doc in result.documents) {
                val post = doc.toObject<Post>()
                post?.let {
                    postList.add(it)
                }
            }
            Resource.Success(postList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addCheckIn(checkIn: CheckIn): Resource<CheckIn> {
        return try {
            val checkInMap = hashMapOf(
                "userId" to checkIn.userId,
                "placeName" to checkIn.placeName,
                "longitude" to checkIn.longitude,
                "latitude" to checkIn.latitude,
                "category" to checkIn.category,
                "date" to checkIn.date,
                "time" to Timestamp.now()
            )

            firebaseFirestore.collection("checkins").add(checkInMap)
            Resource.Success(checkIn)

            Resource.Success(checkIn)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserPosts(userId: String): Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        return try {
            val result = firebaseFirestore.collection("posts").orderBy("time", Query.Direction.DESCENDING)
                .get().await()
            for (doc in result.documents) {
                val post = doc.toObject<Post>()
                if (post?.userId == userId) {
                    postList.add(post)
                }
            }
            Resource.Success(postList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserCheckInList(userId: String): Resource<List<CheckIn>> {
        val checkinList = mutableListOf<CheckIn>()
        return try {
            val result = firebaseFirestore.collection("checkins").orderBy("time", Query.Direction.DESCENDING)
                .get().await()
            for (doc in result.documents) {
                val checkIn = doc.toObject<CheckIn>()
                if (checkIn?.userId == userId) {
                    checkinList.add(checkIn)
                }
            }
            Resource.Success(checkinList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getRecommendationList(): Resource<List<Recommendation>> {
        val list = mutableListOf<Recommendation>()
        return try {
            val result = firebaseFirestore.collection("recommendations")
                .get()
                .await()
            for (doc in result.documents) {
                val recommendation = Recommendation().apply {
                    title = doc.getString("title")
                    image = doc.getString("image")
                    documentId = doc.id
                }
                list.add(recommendation)
            }
            Resource.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getRecommendationById(docId: String): Resource<Recommendation> {
        val recommendation = Recommendation()
        return try {
            val result = firebaseFirestore.collection("recommendations").document(docId)
                .get()
                .await()
            recommendation.apply {
                title = result.getString("title")
                content = result.getString("content")
                image = result.getString("image")
                documentId = result.id
                imageList = result.get("imageList") as List<String>
            }
            val places = result.get("placeList") as List<DocumentReference>
            val placeList = mutableListOf<Location?>()
            for (ref in places) {
                val res = ref.get().await()
                placeList.add(
                    Location().apply {
                        locationName = res.getString("locationName")
                        latitude = res.getString("latitude")
                        longitude = res.getString("longitude")
                        category = res.getString("category")
                    })
            }
            recommendation.placeList = placeList
            Resource.Success(recommendation)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getLocationList(category: String): Resource<List<Location>> {
        val list = mutableListOf<Location>()
        return try {
            val result = firebaseFirestore.collection("locations")
                .get()
                .await()
            for (doc in result.documents) {
                val location = doc.toObject<Location>()
                if (location?.category == category) {
                    list.add(location)
                }
            }
            Resource.Success(list)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addRecommendation(content: String): Resource<String> {
        return try {
            val userMap = hashMapOf(
                "content" to content
            )

            firebaseFirestore.collection("recommendations")
                .add(userMap).await()
            Resource.Success(content)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}