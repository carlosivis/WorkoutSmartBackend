package dev.carlosivis.features.auth

import dev.carlosivis.core.Routes
import dev.carlosivis.core.Strings
import dev.carlosivis.core.onFailure
import dev.carlosivis.core.onSuccess
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {

    val service by inject<AuthService>()

    route(Routes.Auth.BASE) {
        post(Routes.Auth.LOGIN) {
            val firebaseUid = call.principal<UserIdPrincipal>()?.name

            if (firebaseUid == null) {
                call.respond(HttpStatusCode.Unauthorized, Strings.Security.UNAUTHORIZED)
                return@post
            }

            val loginRequest = try {
                call.receive<LoginRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, Strings.Error.badRequest(e.message))
                return@post
            }

            service.registerOrLogin(firebaseUid, loginRequest)
                .onSuccess { userResponse ->
                    call.respond(HttpStatusCode.OK, userResponse)
                }
                .onFailure { e ->
                    e.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError,
                        mapOf("error" to Strings.Error.UNEXPECTED_ERROR))
                }
        }
    }
}
