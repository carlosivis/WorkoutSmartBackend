package dev.carlosivis.features.activity

import dev.carlosivis.core.Either
import dev.carlosivis.core.runCatchingEither
import dev.carlosivis.features.auth.Users
import dev.carlosivis.features.group.GroupMembers
import dev.carlosivis.features.group.Groups
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object ActivityService {

    private const val POINTS_PER_WORKOUT = 10L

    fun logActivity(userId: Int, request: LogActivityRequest): Either<Unit> = runCatchingEither {
        transaction {
            Activities.insert {
                it[this.userId] = userId
                it[this.type] = request.type
                it[this.description] = request.description
            }

            val userGroups = GroupMembers.select(GroupMembers.groupId)
                .where { GroupMembers.userId eq userId }
                .map { it[GroupMembers.groupId] }

            if (userGroups.isNotEmpty()) {
                GroupMembers.update({ (GroupMembers.userId eq userId) and (GroupMembers.groupId inList userGroups) }) {
                    with(SqlExpressionBuilder) {
                        it.update(score, score + POINTS_PER_WORKOUT)
                    }
                }
            }
        }
    }
}