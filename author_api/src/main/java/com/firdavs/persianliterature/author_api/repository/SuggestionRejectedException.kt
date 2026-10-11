package com.firdavs.persianliterature.author_api.repository

/**
 * Thrown when the backend refuses the suggestion: the user is on the ban list,
 * App Check did not attest the request, or the payload broke the security rules.
 */
class SuggestionRejectedException(cause: Throwable) : Exception(cause)
