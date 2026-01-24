package dev.carlosivis.plugins

import dev.carlosivis.features.activity.Activities
import dev.carlosivis.features.auth.Users
import dev.carlosivis.features.group.GroupMembers
import dev.carlosivis.features.group.Groups
import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val url = environment.config.propertyOrNull("postgres.url")?.getString()

    val database = if (!url.isNullOrBlank() && url.contains("postgresql")) {
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()
        log.info("Conectando ao Postgres em: $url")
        Database.connect(url, driver = "org.postgresql.Driver", user = user, password = password)
    } else {
        log.info("Usando banco em memória H2")
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )
    }

    transaction(database) {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Users, Groups, GroupMembers, Activities)
    }

}

/**
 * Makes a connection to a Postgres database.
 *
 * In order to connect to your running Postgres process,
 * please specify the following parameters in your configuration file:
 * - postgres.url -- Url of your running database process.
 * - postgres.user -- Username for database connection
 * - postgres.password -- Password for database connection
 *
 * If you don't have a database process running yet, you may need to [download]((https://www.postgresql.org/download/))
 * and install Postgres and follow the instructions [here](https://postgresapp.com/).
 * Then, you would be able to edit your url,  which is usually "jdbc:postgresql://host:port/database", as well as
 * user and password values.
 *
 *
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun Application.connectToPostgres(embedded: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (embedded) {
        log.info("Using embedded H2 database for testing; replace this flag to use postgres")
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "root", "")
    } else {
        val url = environment.config.property("postgres.url").getString()
        log.info("Connecting to postgres database at $url")
        val user = environment.config.property("postgres.user").getString()
        val password = environment.config.property("postgres.password").getString()

        return DriverManager.getConnection(url, user, password)
    }
}
