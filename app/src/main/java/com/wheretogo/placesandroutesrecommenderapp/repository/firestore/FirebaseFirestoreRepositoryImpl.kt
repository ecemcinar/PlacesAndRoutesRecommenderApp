package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import com.google.firebase.firestore.FirebaseFirestore
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
                "nameAndSurname" to user.nameAndSurname,
                "email" to user.email
            )

            val result = firebaseFirestore.collection("users").document(user.userId!!)
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
            val result = firebaseFirestore.collection("users").document(documentId)
                .update(userMap as Map<String, Any>).await()
            Resource.Success(prefList)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun addPost(userEmail: String, post: Post): Resource<Post> {
        return try {
            val postMap = hashMapOf(
                "userEmail" to userEmail,
                "title" to post.title,
                "content" to post.content
            )

            val result = firebaseFirestore.collection("posts")
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

            val result = firebaseFirestore.collection("users").document(documentId)
                .update(data as Map<String, Any>).await()
            Resource.Success(res)
        }
        catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}