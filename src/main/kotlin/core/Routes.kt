package dev.carlosivis.core

object Routes {

    object Auth {
        const val BASE = "/auth"
        const val REGISTER = "/register"
        const val LOGIN = "/login"
    }

    object Groups {
        const val BASE = "/groups"
        const val CREATE = "/"
        const val JOIN = "/join"
        const val RANKING = "/ranking"
    }

    object Activities {
        const val BASE = "/activities"
    }
}