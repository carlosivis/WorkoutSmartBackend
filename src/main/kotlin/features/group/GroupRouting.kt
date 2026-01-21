package dev.carlosivis.features.group


import dev.carlosivis.core.Routes
import dev.carlosivis.core.Strings
import dev.carlosivis.core.onFailure
import dev.carlosivis.core.onSuccess
import dev.carlosivis.features.auth.Users
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.groupRoutes() {
    route(Routes.Groups.BASE) {
        post(Routes.Groups.CREATE) {
            val firebaseUid =
                call.principal<UserIdPrincipal>()?.name ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val userId = getUserIdFromFirebaseUid(firebaseUid)

            val request = call.receive<CreateGroupRequest>()
            GroupService.create(userId, request)
                .onSuccess{
                        groupId ->
                    call.respond(HttpStatusCode.Created, mapOf("groupId" to groupId))
                }
                .onFailure {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message))
                }
        }

        get {
            val firebaseUid =
                call.principal<UserIdPrincipal>()?.name ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val userId = getUserIdFromFirebaseUid(firebaseUid)

            GroupService.listUserGroups(userId)
                .onSuccess { groups ->
                    call.respond(groups)
                }
                .onFailure {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message))
                }

        }

        post(Routes.Groups.JOIN) {
            val firebaseUid =
                call.principal<UserIdPrincipal>()?.name ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val userId = getUserIdFromFirebaseUid(firebaseUid)

            val request = call.receive<JoinGroupRequest>()


                GroupService.join(userId, request.inviteCode)
                    .onSuccess { group ->
                        call.respond(HttpStatusCode.OK, mapOf("message" to Strings.Groups.JOIN_SUCCESS))
                    }
                    .onFailure { error ->
                        val status = when(error) {
                            is IllegalArgumentException -> HttpStatusCode.NotFound
                            is IllegalStateException -> HttpStatusCode.Conflict
                            else -> HttpStatusCode.BadRequest
                        }
                        call.respond(status, mapOf("error" to error.message))
                    }
        }
    }
}

private fun getUserIdFromFirebaseUid(uid: String): Int = transaction {
    Users.select(Users.id).where { Users.firebaseUid eq uid }.single()[Users.id]
}