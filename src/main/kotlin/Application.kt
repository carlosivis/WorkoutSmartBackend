package dev.carlosivis

import dev.carlosivis.plugins.configureDatabases
import dev.carlosivis.plugins.configureRouting
import dev.carlosivis.plugins.configureSecurity
import dev.carlosivis.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureDatabases()
    configureRouting()
}
