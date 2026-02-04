package dev.carlosivis.features.auth

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val firebaseUid = varchar("firebase_uid", 128).uniqueIndex()
    val email = varchar("email", 100)
    val displayName = varchar("display_name", 100).nullable()
    val points = long("points").default(0)
    val photoUrl = varchar("photo_url", 1024).nullable()

    override val primaryKey = PrimaryKey(id)
}