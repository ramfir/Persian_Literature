package com.firdavs.persianliterature.quiz.model

import com.firdavs.persianliterature.quiz.db.model.QuestionEntity
import com.google.firebase.firestore.PropertyName

data class QuestionDTO(
    val id: String = "",
    @get:PropertyName("quizId") @set:PropertyName("quizId")
    var quizId: String = "",
    @get:PropertyName("questionText") @set:PropertyName("questionText")
    var questionText: String = "",
    @get:PropertyName("options") @set:PropertyName("options")
    var options: List<String> = emptyList(),
    @get:PropertyName("correctAnswer") @set:PropertyName("correctAnswer")
    var correctAnswer: String = "",
    @get:PropertyName("explanation") @set:PropertyName("explanation")
    var explanation: String = "",
    @get:PropertyName("order") @set:PropertyName("order")
    var order: Int = 0,
    @get:PropertyName("relatedAuthorId") @set:PropertyName("relatedAuthorId")
    var relatedAuthorId: String? = null
)

fun QuestionDTO.toDb() = QuestionEntity(
    id = id,
    quizId = quizId,
    questionText = questionText,
    options = options,
    correctAnswer = correctAnswer,
    explanation = explanation,
    order = order,
    relatedAuthorId = relatedAuthorId
)

fun List<QuestionDTO>.toDb() = map { it.toDb() }
