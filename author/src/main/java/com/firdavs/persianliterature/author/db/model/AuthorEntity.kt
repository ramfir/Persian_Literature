package com.firdavs.persianliterature.author.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.author.db.AuthorsDb

@Entity(tableName = AuthorsDb.AUTHORS)
data class AuthorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String?,
    val born: String,
    val died: String,
    val place: String,
    val bioUrl: String?,
    val isFavourite: Boolean = false
)
