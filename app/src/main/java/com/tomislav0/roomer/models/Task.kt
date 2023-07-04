package com.tomislav0.roomer.models

import com.google.type.DateTime
import java.util.UUID.randomUUID


data class Task(
    val id: String = randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val assignedTo: List<User> = listOf(),
    val deadline: DateTime,
    val isDone: Boolean = false
)