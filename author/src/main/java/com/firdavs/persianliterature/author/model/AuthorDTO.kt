package com.firdavs.persianliterature.author.model

import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.google.firebase.firestore.PropertyName

data class AuthorDTO(
    val id: String = "",
    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl")
    var imageUrl: String? = null,
    @get:PropertyName("birthDate") @set:PropertyName("birthDate")
    var birthDate: String = "",
    @get:PropertyName("deathDate") @set:PropertyName("deathDate")
    var deathDate: String = "",
    @get:PropertyName("birthPlace") @set:PropertyName("birthPlace")
    var birthPlace: String = "",
    @get:PropertyName("bioUrl") @set:PropertyName("bioUrl")
    var bioUrl: String? = null
)

fun List<AuthorDTO>.toDb() = map { it.toDb() }

fun AuthorDTO.toDb() = AuthorEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    birthDate = birthDate,
    deathDate = deathDate,
    birthPlace = birthPlace,
    bioUrl = bioUrl
)
