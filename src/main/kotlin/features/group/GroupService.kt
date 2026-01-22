package dev.carlosivis.features.group

import dev.carlosivis.core.Either
import dev.carlosivis.core.Strings
import dev.carlosivis.core.runCatchingEither
import dev.carlosivis.features.auth.Users
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object GroupService {

    fun create(userId: Int, request: CreateGroupRequest): Either<Int> = runCatchingEither {
        transaction {
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
    }

    fun join(userId: Int, code: String): Either<Unit> = runCatchingEither {
        transaction {
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
        }
    }

    fun listUserGroups(userId: Int) = runCatchingEither {
        transaction {
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

    fun getGroupRanking(groupId: Int, requestingUserId: Int): Either<List<RankingMember>> = runCatchingEither {
        transaction {
            val groupExists = Groups.select(Groups.id).where { Groups.id eq groupId }.count() > 0
            if (!groupExists) {
                throw IllegalArgumentException(Strings.Groups.NOT_FOUND)
            }

            val isMember = GroupMembers.selectAll().where {
                (GroupMembers.groupId eq groupId) and (GroupMembers.userId eq requestingUserId)
            }.count() > 0

            if (!isMember) {
                throw IllegalAccessException(Strings.Error.NOT_PERMISSION)
            }

            (Users innerJoin GroupMembers)
                .select(Users.displayName, GroupMembers.score)
                .where { GroupMembers.groupId eq groupId }
                .orderBy(GroupMembers.score, SortOrder.DESC)
                .mapIndexed { index, row ->
                    RankingMember(
                        position = index + 1,
                        displayName = row[Users.displayName] ?: "John Doe",
                        score = row[GroupMembers.score]
                    )
                }
        }
    }
}