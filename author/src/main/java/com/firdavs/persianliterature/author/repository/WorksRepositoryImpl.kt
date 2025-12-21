package com.firdavs.persianliterature.author.repository

import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.model.WorkEntity
import com.firdavs.persianliterature.author.db.model.toDomain
import com.firdavs.persianliterature.author.model.WorkDTO
import com.firdavs.persianliterature.author.model.toDb
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Locale

class WorksRepositoryImpl(
    private val worksDao: WorksDao
) : WorksRepository {
    override suspend fun fetchWorks() {
        val lang = Locale.getDefault().language
        val worksCollection = Firebase.firestore.collection("works_$lang")
        val snapshot = worksCollection.get(Source.SERVER).await()
        val worksDTO = snapshot.documents.mapNotNull { document ->
            val workDto = document.toObject(WorkDTO::class.java)
            workDto?.copy(id = document.id)
        }
        worksDao.insert(worksDTO.toDb())
    }

    override fun getWorksByAuthorId(authorId: String): Flow<List<Work>> {
        return worksDao.getByAuthorIdFlow(authorId).map { works: List<WorkEntity> ->
            works.toDomain()
        }
    }

    override fun getWork(id: String): Flow<Work> {
        return worksDao.getByIdFlow(id).map {
            it.toDomain()
        }
    }
}
