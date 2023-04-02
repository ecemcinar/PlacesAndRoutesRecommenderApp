package com.wheretogo.placesandroutesrecommenderapp.repository.firestore

import com.google.firebase.firestore.FirebaseFirestore
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
                "nameAndSurname" to user.nameAndSurname,
                "email" to user.email
            )

            val result = firebaseFirestore.collection("users")
                .add(userMap).await()
            Resource.Success(user)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}