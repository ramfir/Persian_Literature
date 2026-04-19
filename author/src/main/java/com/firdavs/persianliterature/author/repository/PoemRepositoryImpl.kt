package com.firdavs.persianliterature.author.repository

import android.content.Context
import com.firdavs.persianliterature.author.db.dao.PoemsDao
import com.firdavs.persianliterature.author.db.mapper.toDomain
import com.firdavs.persianliterature.author.model.PoemDTO
import com.firdavs.persianliterature.author.model.toDb
import com.firdavs.persianliterature.author_api.model.Poem
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class PoemRepositoryImpl(
    private val poemsDao: PoemsDao,
    private val languageManager: LanguageManager,
    private val context: Context
) : PoemRepository {

    override suspend fun fetchPoems() {
        val lang = languageManager.getSavedLanguage(context).firebaseCode
        val poemsCollection = Firebase.firestore.collection("poems_$lang")
        val snapshot = poemsCollection.get(Source.SERVER).await()
        val poemsDTO = snapshot.documents.mapNotNull { document ->
            val poemDto = document.toObject(PoemDTO::class.java)
            poemDto?.copy(id = document.id)
        }
        poemsDao.deleteAll()
        poemsDao.insert(poemsDTO.toDb())
    }

    override suspend fun getRandomPoem(): Poem? {
        return poemsDao.getRandomPoem()?.toDomain()
    }

    override suspend fun getPoemById(id: String): Poem? {
        return poemsDao.getPoemById(id)?.toDomain()
    }

    override suspend fun getAllPoems(): List<Poem> {
        return poemsDao.getAllPoems().map { it.toDomain() }
    }
}
