package com.firdavs.persianliterature.settings.api

enum class Language(val code: String, val displayName: String, val firebaseCode: String = code) {
    ENGLISH("en", "English"),
    RUSSIAN("ru", "Русский"),
    TAJIK("tg", "Тоҷикӣ", "tj")
}
