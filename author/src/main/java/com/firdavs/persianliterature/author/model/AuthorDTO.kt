package com.firdavs.persianliterature.author.model

import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.google.firebase.firestore.PropertyName

data class AuthorDTO(
    val id: String = "",
    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl")
    var imageUrl: String? = null,
    @get:PropertyName("born") @set:PropertyName("born")
    var born: String = "",
    @get:PropertyName("died") @set:PropertyName("died")
    var died: String = "",
    @get:PropertyName("place") @set:PropertyName("place")
    var place: String = "",
    @get:PropertyName("bioUrl") @set:PropertyName("bioUrl")
    var bioUrl: String? = null
)

fun List<AuthorDTO>.toDb() = map { it.toDb() }

fun AuthorDTO.toDb() = AuthorEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    born = born,
    died = died,
    place = place,
    bioUrl = bioUrl
)
