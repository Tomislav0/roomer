package com.tomislav0.roomer.dataAccess

import com.google.firebase.auth.FirebaseUser
import com.tomislav0.roomer.models.Response
import com.tomislav0.roomer.models.User

typealias AddUserResponse = Response<Boolean>
typealias GetUsersResponse = Response<List<User>>

interface UserRepository {
    suspend fun addUser(user: User): AddUserResponse
    suspend fun getAllUsers(): List<User>

}