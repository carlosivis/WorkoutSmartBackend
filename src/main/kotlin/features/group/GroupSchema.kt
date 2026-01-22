package dev.carlosivis.features.group

import dev.carlosivis.features.auth.Users
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Groups : Table("groups") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val description = varchar("description", 255).nullable()
    val inviteCode = varchar("invite_code", 10).uniqueIndex()
    val ownerId = integer("owner_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}

object GroupMembers : Table("group_members") {
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val groupId = integer("group_id").references(Groups.id, onDelete = ReferenceOption.CASCADE)
    val score = long("score").default(0)
    val joinedAt = datetime("joined_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(userId, groupId)
}