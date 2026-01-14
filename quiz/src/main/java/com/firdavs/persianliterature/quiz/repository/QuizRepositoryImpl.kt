package com.firdavs.persianliterature.quiz.repository

import android.content.Context
import com.firdavs.persianliterature.quiz.db.dao.QuizDao
import com.firdavs.persianliterature.quiz.db.model.toDomain
import com.firdavs.persianliterature.quiz.model.QuizDTO
import com.firdavs.persianliterature.quiz.model.toDb
import com.firdavs.persianliterature.quiz_api.model.Quiz
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class QuizRepositoryImpl(
    private val quizDao: QuizDao,
    private val languageManager: LanguageManager,
    private val context: Context
) : QuizRepository {

    override suspend fun fetchQuizzes() {
        val lang = languageManager.getSavedLanguage(context).firebaseCode
        val quizzesCollection = Firebase.firestore.collection("quizzes_$lang")
        val snapshot = quizzesCollection.get(Source.SERVER).await()
        val quizzesDTO = snapshot.documents.mapNotNull { document ->
            val quizDto = document.toObject(QuizDTO::class.java)
            quizDto?.copy(id = document.id)
        }
        quizDao.insert(quizzesDTO.toDb())
    }

    override fun getQuizzes(): Flow<List<Quiz>> {
        return quizDao.getAllFlow().map { it.toDomain() }
    }

    override fun getQuiz(id: String): Flow<Quiz> {
        return quizDao.getByIdFlow(id).map { it.toDomain() }
    }
}
