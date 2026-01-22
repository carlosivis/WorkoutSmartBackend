package dev.carlosivis.features.group


import dev.carlosivis.core.*
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
           UserUtils.withUser(call) { userId ->
               val request = call.receive<CreateGroupRequest>()
               GroupService.create(userId, request)
                   .onSuccess { groupId ->
                       call.respond(HttpStatusCode.Created, mapOf("groupId" to groupId))
                   }
                   .onFailure {
                       call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message))
                   }
           }
        }

        get {
            UserUtils.withUser(call) { userId ->
                GroupService.listUserGroups(userId)
                    .onSuccess { groups ->
                        call.respond(groups)
                    }
                    .onFailure {
                        call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message))
                    }
            }
        }

        post(Routes.Groups.JOIN) {
            UserUtils.withUser(call){ userId ->
                val request = call.receive<JoinGroupRequest>()

                GroupService.join(userId, request.inviteCode)
                    .onSuccess { group ->
                        call.respond(HttpStatusCode.OK, mapOf("message" to Strings.Groups.JOIN_SUCCESS))
                    }
                    .onFailure { error ->
                        val status = when (error) {
                            is IllegalArgumentException -> HttpStatusCode.NotFound
                            is IllegalStateException -> HttpStatusCode.Conflict
                            else -> HttpStatusCode.BadRequest
                        }
                        call.respond(status, mapOf("error" to error.message))
                    }
            }
        }

        get("/{id}/${Routes.Groups.RANKING}") {
            UserUtils.withUser(call) { userId ->
                val groupId = call.parameters["id"]?.toIntOrNull()

                if (groupId == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID do grupo inválido"))
                    return@withUser
                }
                GroupService.getGroupRanking(groupId, userId)
                    .onSuccess { ranking ->
                        call.respond(ranking)
                    }
                    .onFailure { error ->
                        val status = when (error) {
                            is IllegalAccessException -> HttpStatusCode.Forbidden
                            is IllegalArgumentException -> HttpStatusCode.NotFound
                            else -> HttpStatusCode.BadRequest
                        }
                        call.respond(status, mapOf("error" to error.message))
                    }
            }
        }
    }
}