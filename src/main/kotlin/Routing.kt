package dev.carlosivis

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*

@Serializable
data class AuthResponse(val message: String, val uid: String? = null)

@Serializable
data class VerifyTokenRequest(val token: String)

fun Application.configureRouting() {
    val firebaseAuth = FirebaseAuth.getInstance()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/auth/verify-token") {
            try {
                val request = call.receive<VerifyTokenRequest>()
                val decodedToken = firebaseAuth.verifyIdToken(request.token)
                call.respond(HttpStatusCode.OK, AuthResponse("Token válido", decodedToken.uid))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized, AuthResponse("Token inválido"))
            }
        }

        authenticate("firebase") {
            get("/protected") {
                val principal = call.principal<UserIdPrincipal>()
                call.respond(AuthResponse("Acesso concedido", principal?.name))
            }
        }
    }
}
