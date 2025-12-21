package com.firdavs.persianliterature.util.coroutines

import android.util.Log
import kotlinx.coroutines.delay

suspend fun runWithRetry(
    maxAttempts: Int = 3,
    intervalMillis: Long = 1000,
    block: suspend () -> Unit
): Throwable? {
    var attempts = maxAttempts
    var error: Throwable? = null
    while (attempts != 0) {
        try {
            block()
            return null
        } catch (e: Throwable) {
            error = e
            Log.w("RetryUtils", "Attempt failed: $e")
        }
        if (--attempts != 0) {
            delay(intervalMillis)
            Log.d("RetryUtils", "Retrying... $attempts attempt(s) left")
        }
    }
    return error
}
