package dev.carlosivis.features.workoutlog

import dev.carlosivis.features.auth.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object WorkoutLogs : Table("workout_logs") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val type = enumerationByName("type", 50, WorkoutType::class)
    val description = varchar("description", 255).nullable()
    val durationInSeconds = long("duration_in_seconds").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}