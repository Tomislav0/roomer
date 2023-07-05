package com.tomislav0.roomer.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val surname: String,
    val initials: String,
    val gender: String,
) {
    constructor() : this("", "", "", "", "", "")
}
