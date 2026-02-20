package dev.carlosivis.di

import dev.carlosivis.features.auth.AuthService
import dev.carlosivis.features.group.GroupService
import dev.carlosivis.features.workoutlog.WorkoutLogService
import org.koin.dsl.module

val appModule = module {
    single { AuthService }
    single { GroupService }
    single { WorkoutLogService }
}