package dev.carlosivis.features.group

import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    val name: String,
    val description: String? = null
)

@Serializable
data class JoinGroupRequest(
    val inviteCode: String
)

@Serializable
data class GroupResponse(
    val id: Int,
    val name: String,
    val inviteCode: String,
    val memberCount: Long
)

@Serializable
data class RankingMember(
    val position: Int,
    val displayName: String,
    val score: Long
)