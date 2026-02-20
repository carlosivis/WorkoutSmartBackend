package dev.carlosivis

import dev.carlosivis.plugins.configureDI
import dev.carlosivis.plugins.configureDatabases
import dev.carlosivis.plugins.configureRouting
import dev.carlosivis.plugins.configureSecurity
import dev.carlosivis.plugins.configureSerialization
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*

fun main(args: Array<String>) {

    if (System.getenv("RUNNING_IN_DOCKER") == null)
        dotenv {
            ignoreIfMissing = true
        }.entries().forEach {
            System.setProperty(it.key, it.value)
        }

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureSecurity()
    configureSerialization()
    configureDatabases(false)
    configureRouting()
}
