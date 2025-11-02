package com.example.bostontour.nav

sealed class Route(val base: String, val route: String) {
    object Home       : Route("home", "home")
    object Categories : Route("categories", "categories")
    object List       : Route("list", "list/{category}") {
        fun path(category: String) = "list/$category"
    }
    object Detail     : Route("detail", "detail/{category}/{placeId}") {
        fun path(category: String, placeId: Int) = "detail/$category/$placeId"
    }
}