package com.tomislav0.roomer.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.tomislav0.roomer.dataAccess.AuthRepository
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.Room
import com.tomislav0.roomer.models.Task
import com.tomislav0.roomer.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun createRoom(room: Room) = viewModelScope.launch {
        FirebaseFirestore.getInstance().collection("rooms").document(room.id).set(room).await()
    }

    var rooms  = mutableStateOf<List<Room>>(listOf())
    var room = mutableStateOf<Room?>(Room())

    fun getRooms(currUser:User) = viewModelScope.launch {
        rooms.value = FirebaseFirestore.getInstance().collection("rooms").whereArrayContains("members",currUser).get().await()
            .toObjects(Room::class.java)
    }

    fun getRoom(id:String) = viewModelScope.launch {
        room.value = FirebaseFirestore.getInstance().collection("rooms").document(id).get().await().toObject<Room>()
    }

    fun createTask(roomId: String, task: Task) = viewModelScope.launch {
        Log.v("Debug", "CreateRoom")
        val room = FirebaseFirestore.getInstance().collection("rooms").document(roomId).get().await().toObject<Room>()
        room!!
        val nRoom = Room(room.id,room.name,room.description,room.members, room.tasks + listOf(task))
        FirebaseFirestore.getInstance().collection("rooms").document(roomId).set(nRoom).await()
    }



}
