package com.firdavs.persianliterature.author.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.author.db.AuthorsDb

@Entity(tableName = AuthorsDb.POEMS)
data class PoemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val text: String,
    val author: String,
    val isFavourite: Boolean = false
)
