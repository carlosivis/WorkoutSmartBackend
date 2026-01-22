package dev.carlosivis.core

import io.ktor.http.ContentType

object Strings {

    object Security {
        const val FIREBASE_CONFIG_MISSING = "Arquivo 'firebase-admin.json' não encontrado nos resources! Verifique a configuração."
        const val UNAUTHORIZED = "Acesso não autorizado ou token inválido."
    }

    object Auth {
        const val USER_NOT_FOUND = "Usuário não encontrado."
        const val LOGIN_SUCCESS = "Login realizado com sucesso."
    }

    object Groups {
        const val NOT_FOUND = "Grupo não encontrado."
        const val ALREADY_MEMBER = "Você já faz parte deste grupo."
        const val JOIN_SUCCESS = "Você entrou no grupo com sucesso!"
        const val CREATE_SUCCESS = "Grupo criado com sucesso."
    }

    object Activity {
        const val REGISTRY_SUCCESS = "Registro de atividade realizado com sucesso!"
    }

    object Error {
        const val UNEXPECTED_ERROR = "Ocorreu um erro inesperado."
        const val NOT_PERMISSION = "Você não tem permissão para visualizar este ranking."
        fun badRequest(message: String?) = "Requisição inválido: $message"
    }

}