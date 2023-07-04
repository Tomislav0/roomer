package com.tomislav0.roomer.dataAccess

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.Room
import com.tomislav0.roomer.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : RoomRepository {

    override suspend fun createRoom(room: Room) {
        try {
            db.collection("rooms").document(room.id).set(room).await()
        } catch (e: Exception) {
            Log.v("Error",e.toString())
        }
    }
}