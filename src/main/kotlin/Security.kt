package dev.carlosivis

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import org.jetbrains.exposed.sql.*

fun Application.configureSecurity() {
    val jwtRealm = "ktor sample app"

    // Inicializar Firebase (certifique-se de ter serviceAccountKey.json no classpath)
    if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp()
    }
    val firebaseAuth = FirebaseAuth.getInstance()

    authentication {
        bearer("firebase") {
            realm = jwtRealm
            authenticate { tokenCredential ->
                try {
                    val decodedToken = firebaseAuth.verifyIdToken(tokenCredential.token)
                    UserIdPrincipal(decodedToken.uid)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}
