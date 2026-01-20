package dev.carlosivis.features.group


import dev.carlosivis.features.auth.Users
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.groupRoutes() {
    route("/groups") {
        authenticate {

            post {
                val firebaseUid = call.principal<UserIdPrincipal>()?.name ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val userId = getUserIdFromFirebaseUid(firebaseUid)

                val request = call.receive<CreateGroupRequest>()
                val groupId = GroupService.create(userId, request)

                call.respond(HttpStatusCode.Created, mapOf("groupId" to groupId))
            }

            get {
                val firebaseUid = call.principal<UserIdPrincipal>()?.name ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val userId = getUserIdFromFirebaseUid(firebaseUid)

                val groups = GroupService.listUserGroups(userId)
                call.respond(groups)
            }

            post("/join") {
                val firebaseUid = call.principal<UserIdPrincipal>()?.name ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val userId = getUserIdFromFirebaseUid(firebaseUid)

                val request = call.receive<JoinGroupRequest>()

                try {
                    GroupService.join(userId, request.inviteCode)
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Entrou com sucesso!"))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                }
            }
        }
    }
}

private fun getUserIdFromFirebaseUid(uid: String): Int = transaction {
    Users.select(Users.id).where { Users.firebaseUid eq uid }.single()[Users.id]
}