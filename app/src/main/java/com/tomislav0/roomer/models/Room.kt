package com.tomislav0.roomer.models

import kotlinx.serialization.Serializable
import java.util.UUID.randomUUID

@Serializable
data class Room(
    val id: String = randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val members: List<User> = listOf(),
    var tasks: List<Task> = listOf()
)