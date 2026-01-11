package com.firdavs.persianliterature.quiz.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz_api.model.Question

@Entity(tableName = QuizDb.QUESTIONS)
data class QuestionEntity(
    @PrimaryKey val id: String,
    val quizId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String,
    val order: Int,
    val relatedAuthorId: String?
)

fun QuestionEntity.toDomain() = Question(
    id = id,
    quizId = quizId,
    questionText = questionText,
    options = options,
    correctAnswer = correctAnswer,
    explanation = explanation,
    order = order,
    relatedAuthorId = relatedAuthorId
)

fun List<QuestionEntity>.toDomain() = map { it.toDomain() }
