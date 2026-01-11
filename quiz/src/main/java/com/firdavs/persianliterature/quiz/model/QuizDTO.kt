package com.firdavs.persianliterature.quiz.model

import com.firdavs.persianliterature.quiz.db.model.QuizEntity
import com.google.firebase.firestore.PropertyName

data class QuizDTO(
    val id: String = "",
    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",
    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",
    @get:PropertyName("difficulty") @set:PropertyName("difficulty")
    var difficulty: String = "BEGINNER",
    @get:PropertyName("category") @set:PropertyName("category")
    var category: String = "MATCH_POET_TO_POEM",
    @get:PropertyName("totalQuestions") @set:PropertyName("totalQuestions")
    var totalQuestions: Int = 0,
    @get:PropertyName("passingScore") @set:PropertyName("passingScore")
    var passingScore: Int = 60,
    @get:PropertyName("order") @set:PropertyName("order")
    var order: Int = 0
)

fun QuizDTO.toDb() = QuizEntity(
    id = id,
    title = title,
    description = description,
    difficulty = difficulty,
    category = category,
    totalQuestions = totalQuestions,
    passingScore = passingScore,
    order = order
)

fun List<QuizDTO>.toDb() = map { it.toDb() }
