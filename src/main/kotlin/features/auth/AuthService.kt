package dev.carlosivis.features.auth

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object AuthService {

    fun registerOrLogin(firebaseUid: String, request: LoginRequest): UserResponse = transaction {
        val existingUser = Users.selectAll().where { Users.firebaseUid eq firebaseUid }.singleOrNull()

        if (existingUser != null) {
            if (existingUser[Users.email] != request.email || existingUser[Users.displayName] != request.displayName) {
                Users.update({ Users.firebaseUid eq firebaseUid }) {
                    it[email] = request.email
                    it[displayName] = request.displayName
                }
            }

            existingUser.toUserResponse()
        } else {
            val newId = Users.insert {
                it[this.firebaseUid] = firebaseUid
                it[this.email] = request.email
                it[this.displayName] = request.displayName
                it[this.points] = 0
            } get Users.id

            UserResponse(
                id = newId,
                firebaseUid = firebaseUid,
                email = request.email,
                displayName = request.displayName,
                points = 0
            )
        }
    }

    private fun ResultRow.toUserResponse() = UserResponse(
        id = this[Users.id],
        firebaseUid = this[Users.firebaseUid],
        email = this[Users.email],
        displayName = this[Users.displayName],
        points = this[Users.points]
    )
}