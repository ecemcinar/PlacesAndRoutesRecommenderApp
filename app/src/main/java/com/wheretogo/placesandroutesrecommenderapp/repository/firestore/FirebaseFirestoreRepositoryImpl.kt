package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.wheretogo.placesandroutesrecommenderapp.model.Post
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
                "content" to post.content
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

    override suspend fun getUser(documentId: String): User? {
        var user: User? = User()
        return try {
            firebaseFirestore.collection("users").document(documentId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        user = it.toObject()
                    }
                }
                .addOnFailureListener {
                    Log.d("Hata", it.message, it)
                }
            user
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
            user
        }
    }

    override suspend fun getPosts(): Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        return try {
            val result = firebaseFirestore.collection("posts")
                .get().await()
            for (doc in result.documents) {
                val post = doc.toObject<Post>()
                post?.let {
                    postList.add(post)
                }
            }
            Resource.Success(postList)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}