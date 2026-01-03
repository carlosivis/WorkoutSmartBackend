package dev.carlosivis.core

sealed class Either<out T> {

    val isSuccess get() = this is Success
    val isFailure get() = this is Failure

    data class Success<T>(val data: T) : Either<T>()

    data class Failure(val exception: Throwable) : Either<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Failure -> "Either.Failure(exception = ${this.exception})"
            is Success -> "Either.Success(data = ${this.data})"
        }
    }
}

inline fun <T> Either<T>.onSuccess(action: (T) -> Unit): Either<T> {
    if (this is Either.Success) action(this.data)
    return this
}

inline fun <T> Either<T>.onFailure(action: (Throwable) -> Unit): Either<T> {
    if (this is Either.Failure) action(this.exception)
    return this
}

inline fun <T> runCatchingEither(block: () -> T): Either<T> {
    return try {
        Either.Success(block())
    } catch (e: Throwable) {
        Either.Failure(e)
    }
}