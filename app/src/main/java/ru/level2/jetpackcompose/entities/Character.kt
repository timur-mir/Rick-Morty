package ru.level2.jetpackcompose.entities

import java.io.Serializable

data class Character(
    val id: Long,
    val name: String,
    val species: String,
    val status: String,
    val location: Location,
    val image: String,
    val origin: Origin,
    val episode: List<String>
):Serializable