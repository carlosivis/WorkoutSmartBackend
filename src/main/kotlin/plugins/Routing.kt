package dev.carlosivis.plugins

import dev.carlosivis.features.activity.activityRoutes
import dev.carlosivis.features.auth.authRoutes
import dev.carlosivis.features.group.GroupService
import dev.carlosivis.features.group.groupRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {

    routing {
        authenticate("auth-firebase") {
            authRoutes()

            groupRoutes()

            activityRoutes()
        }

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
