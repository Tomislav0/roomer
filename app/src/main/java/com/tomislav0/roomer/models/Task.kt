package com.tomislav0.roomer.models

import java.util.UUID.randomUUID


data class Task(
    val id: String = randomUUID().toString(),
    val roomName : String= "",
    val roomId : String = "",
    val name: String = "",
    val description: String = "",
    val assignedTo: List<User> = listOf(),
    val deadline: String = "",
    var isDone: Boolean = false
)