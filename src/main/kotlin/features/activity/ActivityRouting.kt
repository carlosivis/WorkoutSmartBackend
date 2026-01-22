package dev.carlosivis.features.activity

import dev.carlosivis.core.Routes
import dev.carlosivis.core.Strings
import dev.carlosivis.core.UserUtils
import dev.carlosivis.core.onFailure
import dev.carlosivis.core.onSuccess
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.activityRoutes() {
    route(Routes.Activities.BASE) {
        post {
            UserUtils.withUser(call) { userId ->
                val request = call.receive<LogActivityRequest>()
                ActivityService.logActivity(userId, request)
                    .onSuccess { call.respond(HttpStatusCode.Created,
                        mapOf("message" to Strings.Activity.REGISTRY_SUCCESS)) }
                    .onFailure { call.respond(HttpStatusCode.BadRequest,
                        mapOf("error" to it.message)) }
            }
        }
    }
}