package dev.carlosivis.features.workoutlog

import kotlinx.serialization.Serializable


enum class WorkoutType {
    GYM,
    RUNNING,
    CYCLING,
    SPORTS,
    CROSSFIT,
    OTHER
}

@Serializable
data class WorkoutLogRequest(
    val type: WorkoutType,
    val description: String? = null,
    val durationInSeconds: Long? = null,
)