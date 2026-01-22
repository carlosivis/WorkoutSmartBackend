package dev.carlosivis.features.activity


import dev.carlosivis.features.auth.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Activities : Table("activities") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    //ex: "GYM", "RUN", "SPORT" - make enum later
    val type = varchar("type", 50)
    val description = varchar("description", 255).nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}