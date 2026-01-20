package dev.carlosivis.features.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.authRoutes() {
    route("/auth") {
        authenticate("auth-firebase") {

            post("/login") {
                val firebaseUid = call.principal<UserIdPrincipal>()?.name

                if (firebaseUid == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Token inválido ou usuário não identificado")
                    return@post
                }

                val loginRequest = try {
                    call.receive<LoginRequest>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Corpo da requisição inválido: ${e.message}")
                    return@post
                }

                try {
                    val userResponse = AuthService.registerOrLogin(firebaseUid, loginRequest)
                    call.respond(HttpStatusCode.OK, userResponse)
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, "Erro ao processar login")
                }
            }
        }
    }
}
private fun getUserIdFromFirebaseUid(uid: String): Int = transaction {
    Users.select(Users.id).where { Users.firebaseUid eq uid }.single()[Users.id]
}