package com.firdavs.persianliterature.app.appcheck

import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

/**
 * Debug builds cannot pass Play Integrity, so they attest with the debug provider.
 * Its token is printed to logcat on the first run and has to be registered in the
 * Firebase Console (App Check -> Manage debug tokens).
 */
fun installAppCheck() {
    FirebaseAppCheck.getInstance()
        .installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
}
