package dev.carlosivis.features.workoutlog

import dev.carlosivis.core.Routes
import dev.carlosivis.core.Strings
import dev.carlosivis.core.UserUtils
import dev.carlosivis.core.onFailure
import dev.carlosivis.core.onSuccess
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.workoutLogRoutes() {
    route(Routes.WorkoutLogs.BASE) {
        post {
            UserUtils.withUser(call) { userId ->
                val request = call.receive<WorkoutLogRequest>()
                WorkoutLogService.logWorkout(userId, request)
                    .onSuccess { call.respond(HttpStatusCode.Created,
                        mapOf("message" to Strings.WorkoutLog.REGISTRY_SUCCESS)) }
                    .onFailure { call.respond(HttpStatusCode.BadRequest,
                        mapOf("error" to it.message)) }
            }
        }
    }
}