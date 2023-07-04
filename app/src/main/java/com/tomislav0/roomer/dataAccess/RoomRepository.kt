package com.tomislav0.roomer.dataAccess

import com.tomislav0.roomer.models.Room

interface RoomRepository {
    suspend fun createRoom(room: Room)

}