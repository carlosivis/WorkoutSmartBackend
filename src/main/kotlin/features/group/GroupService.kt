package dev.carlosivis.features.group

import dev.carlosivis.core.Strings
import dev.carlosivis.features.auth.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object GroupService {

    fun create(userId: Int, request: CreateGroupRequest): Int = transaction {
        // TODO: validate if not exist in prod
        val generatedCode = UUID.randomUUID().toString().substring(0, 6).uppercase()

        val newGroupId = Groups.insert {
            it[name] = request.name
            it[description] = request.description
            it[inviteCode] = generatedCode
            it[ownerId] = userId
        } get Groups.id

        GroupMembers.insert {
            it[this.userId] = userId
            it[this.groupId] = newGroupId
        }

        newGroupId
    }

    fun join(userId: Int, code: String): Boolean = transaction {
        val group = Groups.selectAll().where { Groups.inviteCode eq code }.singleOrNull()
            ?: throw IllegalArgumentException(Strings.Groups.NOT_FOUND)

        val groupIdFound = group[Groups.id]

        val isMember = GroupMembers.selectAll().where {
            (GroupMembers.userId eq userId) and (GroupMembers.groupId eq groupIdFound)
        }.count() > 0

        if (isMember) {
            throw IllegalStateException(Strings.Groups.ALREADY_MEMBER)
        }

        GroupMembers.insert {
            it[this.userId] = userId
            it[this.groupId] = groupIdFound
        }

        true
    }

    fun listUserGroups(userId: Int) = transaction {
        (Groups innerJoin GroupMembers)
            .select(Groups.id, Groups.name, Groups.inviteCode)
            .where { GroupMembers.userId eq userId }
            .map {
                GroupResponse(
                    id = it[Groups.id],
                    name = it[Groups.name],
                    inviteCode = it[Groups.inviteCode],
                    memberCount = 0 // TODO: Implement count real
                )
            }
    }
}