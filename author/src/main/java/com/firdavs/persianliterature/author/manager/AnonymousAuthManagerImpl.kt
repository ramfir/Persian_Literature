package com.firdavs.persianliterature.author.manager

import com.firdavs.persianliterature.author_api.manager.AnonymousAuthManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AnonymousAuthManagerImpl : AnonymousAuthManager {

    override suspend fun getUid(): String {
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.uid?.let { return it }
        val user = auth.signInAnonymously().await().user
        return requireNotNull(user?.uid) { "Anonymous sign-in returned no user" }
    }
}
