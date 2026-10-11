package com.firdavs.persianliterature.author_api.manager

interface AnonymousAuthManager {

    /**
     * Returns the anonymous Firebase uid, signing in silently on the first call.
     * The uid survives app updates and lets a spammer be banned server side.
     */
    suspend fun getUid(): String
}
