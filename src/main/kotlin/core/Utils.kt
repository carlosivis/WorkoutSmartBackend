package dev.carlosivis.core

import dev.carlosivis.features.auth.Users
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.transactions.transaction


object UserUtils {
    private fun getUserIdFromFirebaseUid(uid: String): Int = transaction {
        Users.select(Users.id)
            .where { Users.firebaseUid eq uid }
            .single()[Users.id]
    }

    suspend fun withUser(call: ApplicationCall, block: suspend (userId: Int) -> Unit) {
        val firebaseUid = call.principal<UserIdPrincipal>()?.name

        if (firebaseUid == null) {
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to Strings.Security.UNAUTHORIZED))
            return
        }

        try {
            val userId = getUserIdFromFirebaseUid(firebaseUid)
            block(userId)

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to Strings.Auth.USER_NOT_FOUND))
        }
    }
}