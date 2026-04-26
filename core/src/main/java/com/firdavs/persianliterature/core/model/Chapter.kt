package com.firdavs.persianliterature.core.model

import com.firdavs.persianliterature.core.R

enum class Chapter(val titleRes: Int) {
    Authors(R.string.authors),
    AllWorks(R.string.all_works),
    PoemOfDay(R.string.poem_of_day),
    AudioBooks(R.string.audio_books),
    Quiz(R.string.quiz),
    AboutApp(R.string.about_app),
    Favourites(R.string.favourites),
    Settings(R.string.settings);

    companion object {
        val all = listOf(Authors, AllWorks, PoemOfDay, AudioBooks, Quiz, Favourites, Settings, AboutApp)
    }
}
