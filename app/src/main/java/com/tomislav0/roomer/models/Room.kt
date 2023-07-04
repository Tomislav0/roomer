package com.tomislav0.roomer.models

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: String,
    val name: String,
    val description: String,
    val members: List<User>?
){
    constructor():this("","","", listOf())
}
