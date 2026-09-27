package com.firdavs.persianliterature.app.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.startNewRoot(route: Route) {
    clear()
    next(route)
}

fun NavBackStack<NavKey>.next(route: Route) {
    add(route)
}

fun NavBackStack<NavKey>.back() {
    removeLastOrNull()
}
