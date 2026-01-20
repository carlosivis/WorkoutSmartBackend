package dev.carlosivis.plugins

import com.kborowy.authprovider.firebase.firebase
import dev.carlosivis.core.Strings
import io.ktor.server.application.*
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authentication

fun Application.configureSecurity() {

    authentication {
        firebase("auth-firebase") {
            setup {
                val serviceAccountStream = this::class.java.classLoader
                    .getResourceAsStream("firebase-admin.json")
                    ?: throw IllegalStateException(Strings.Security.FIREBASE_CONFIG_MISSING)

                adminFileStream = serviceAccountStream
            }

            validate { token ->
                UserIdPrincipal(token.uid)
            }
        }
    }

}