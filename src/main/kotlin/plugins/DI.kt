package dev.carlosivis.plugins

import dev.carlosivis.di.appModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    install(Koin){
        slf4jLogger()
        modules(appModule)
    }
}