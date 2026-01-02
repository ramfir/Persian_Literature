package com.firdavs.persianliterature.core.model

import com.firdavs.persianliterature.core.R

enum class Chapter(val titleRes: Int) {
    Authors(R.string.authors),
    AboutApp(R.string.about_app),
    Favourites(R.string.favourites),
    Settings(R.string.settings);

    companion object {
        val all = listOf(Authors, AboutApp, Favourites, Settings)
    }
}
