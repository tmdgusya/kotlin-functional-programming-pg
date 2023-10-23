package testing

import kotlin.IllegalArgumentException
import java.lang.Exception

fun plus(x: Int, y: Int): Either<String, Int> {
    if (x >= Int.MAX_VALUE || y >= Int.MAX_VALUE) return Either.Left("These arguments exceed the maximum value of an integer.")
    return Either.Right(x + y)
}

sealed class Either<out E, out A> {
    data class Left<out E>(val value: E): Either<E, Nothing>()
    data class Right<out A>(val value: A): Either<Nothing, A>()
}

fun <E, A> Either<E, A>.getOrElse(default: A): A = when (this) {
    is Either.Left -> default
    is Either.Right -> value
}

fun <E, A, B> Either<E, A>.map(f: (a: A) -> B): Either<E, B> = when (this) {
    is Either.Right -> Either.Right(f(value))
    is Either.Left -> Either.Left(value)
}

fun <E, A, B> Either<E, A>.flatMap(f: (a: A) -> Either<E, B>): Either<E, B> = when (this) {
    is Either.Right -> f(value)
    is Either.Left -> Either.Left(value)
}

fun <E, A> Either<E, A>.handle(f: (e: E) -> Unit): Either<E, A> = when (this) {
    is Either.Right -> this
    is Either.Left -> this.also { f(value) }
}

fun main() {
    fun plus(a: Int, b: Int): Either<IllegalArgumentException, Int> = when {
        a >= Int.MAX_VALUE -> Either.Left(IllegalArgumentException("a is bigger than the maximum value of an integer"))
        b >= Int.MAX_VALUE -> Either.Left(IllegalArgumentException("b is bigger than the maximum value of an integer"))
        else -> Either.Right(a + b)
    }

    val result = Either.Right(3)
        .flatMap { plus(it, Int.MAX_VALUE) }
        .handle { println(it.message) } // b is bigger than the maximum value of an integer
        .getOrElse(-1)

    println(result == -1) // true
}