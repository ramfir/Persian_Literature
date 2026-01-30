package com.firdavs.persianliterature.quiz.repository

import android.content.Context
import com.firdavs.persianliterature.quiz.db.dao.QuestionDao
import com.firdavs.persianliterature.quiz.db.model.toDomain
import com.firdavs.persianliterature.quiz.model.QuestionDTO
import com.firdavs.persianliterature.quiz.model.toDb
import com.firdavs.persianliterature.quiz_api.model.Question
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class QuestionRepositoryImpl(
    private val questionDao: QuestionDao,
    private val languageManager: LanguageManager,
    private val context: Context
) : QuestionRepository {

    override suspend fun fetchQuestions() {
        val lang = languageManager.getSavedLanguage(context).firebaseCode
        val questionsCollection = Firebase.firestore.collection("questions_$lang")
        val snapshot = questionsCollection.get(Source.SERVER).await()
        val questionsDTO = snapshot.documents.mapNotNull { document ->
            val questionDto = document.toObject(QuestionDTO::class.java)
            questionDto?.copy(id = document.id)
        }
        questionDao.deleteAll()
        questionDao.insert(questionsDTO.toDb())
    }

    override fun getQuestionsByQuizId(quizId: String): Flow<List<Question>> {
        return questionDao.getByQuizIdFlow(quizId).map { it.toDomain() }
    }
}
