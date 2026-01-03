package dev.carlosivis.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(val message: String, val uid: String? = null)

@Serializable
data class VerifyTokenRequest(val token: String)

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
