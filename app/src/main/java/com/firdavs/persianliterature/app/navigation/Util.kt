package com.firdavs.persianliterature.app.navigation

import androidx.navigation3.runtime.NavBackStack

fun NavBackStack.startNewRoot(route: Route) {
    clear()
    next(route)
}

fun NavBackStack.next(route: Route) {
    add(route)
}

fun NavBackStack.back() {
    removeLastOrNull()
}
