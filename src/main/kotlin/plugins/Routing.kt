package dev.carlosivis.plugins

import dev.carlosivis.features.auth.authRoutes
import dev.carlosivis.features.group.groupRoutes
import dev.carlosivis.features.workoutlog.workoutLogRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        authenticate("auth-firebase") {
            authRoutes()

            groupRoutes()

            workoutLogRoutes()
        }

        get("/") {
            call.respondText("Hello World!")
        }
    }
}