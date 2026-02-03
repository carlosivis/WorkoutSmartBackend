package dev.carlosivis.features.auth


import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null
)

@Serializable
data class UserResponse(
    val id: Int,
    val firebaseUid: String,
    val email: String,
    val displayName: String?,
    val points: Long,
    val photoUrl: String?
)