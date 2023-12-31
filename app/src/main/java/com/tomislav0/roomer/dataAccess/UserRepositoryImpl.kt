package com.tomislav0.roomer.dataAccess

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {
    override suspend fun addUser(user: User): AddUserResponse {
        return try {
            db.collection("users").document(user.id)
                .set(user).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return try {
            db.collection("users").get().await().documents as List<User>
        } catch (e: Exception) {
           listOf()
        }
    }

}