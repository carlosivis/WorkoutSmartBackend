package dev.carlosivis.features.workoutlog

import dev.carlosivis.core.Either
import dev.carlosivis.core.runCatchingEither
import dev.carlosivis.features.group.GroupMembers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object WorkoutLogService {


    //TODO(): improve logic about points per type
    private fun getPointsForType(type: WorkoutType): Long {
        return when(type) {
            WorkoutType.GYM -> 10L
            WorkoutType.RUNNING -> 15L
            WorkoutType.CROSSFIT -> 15L
            WorkoutType.SPORTS -> 20L
            WorkoutType.CYCLING -> 10L
            WorkoutType.OTHER -> 10L
        }
    }

    fun logWorkout(userId: Int, request: WorkoutLogRequest): Either<Unit> = runCatchingEither {
        transaction {
            WorkoutLogs.insert {
                it[this.userId] = userId
                it[this.type] = request.type
                it[this.description] = request.description
                it[this.durationInSeconds] = request.durationInSeconds
            }

            val pointsEarned = getPointsForType(request.type)

            val userGroups = GroupMembers.select(GroupMembers.groupId)
                .where { GroupMembers.userId eq userId }
                .map { it[GroupMembers.groupId] }

            if (userGroups.isNotEmpty()) {
                GroupMembers.update({ (GroupMembers.userId eq userId) and (GroupMembers.groupId inList userGroups) }) {
                    with(SqlExpressionBuilder) {
                        it.update(score, score + pointsEarned)
                    }
                }
            }
        }
    }
}