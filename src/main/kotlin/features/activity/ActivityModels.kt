package dev.carlosivis.features.activity


import kotlinx.serialization.Serializable

@Serializable
data class LogActivityRequest(
    val type: String,
    val description: String? = null
)