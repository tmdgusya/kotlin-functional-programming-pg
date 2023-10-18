package chapter04

import chapter3.Cons
import chapter3.List
import chapter3.Nil
import fold_practice.FList
import fold_practice.FList.Nil.foldRight
import fold_practice.foldLeft
import java.lang.Exception

sealed class Either<out E, out A> {
    data class Left<out E>(val value: E): Either<E, Nothing>()
    data class Right<out A>(val value: A): Either<Nothing, A>()

    fun catches(a: () -> @UnsafeVariance A): Either<Exception, A> = try {
        Right(a())
    } catch (e: Exception) {
        Left(e)
    }

    // example 4.6
    inline fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> = when (this) {
        is Left -> this
        is Right -> Right(f(value))
    }

    inline fun <E, A, B> Either<E, A>.flatMap(f: (A) -> Either<E, B>): Either<E, B> = when(this) {
        is Left -> this
        is Right -> f(value)
    }

    inline fun <E, A> Either<E, A>.orElse(f: () -> Either<E, A>): Either<E, A> = when(this) {
        is Left -> f()
        is Right -> this
    }

    inline fun <E, A, B, C> Either<E, A>.map2(other: Either<E, B>, f: (A, B) -> C): Either<E, C> = when(this) {
        is Left -> this
        is Right -> when (other) {
            is Left -> other
            is Right -> Right(f(this.value, other.value))
        }
    }

    // example 4.7
    fun <E, A, B> traverse(
        xs: FList<A>,
        f: (A) -> Either<E, B>
    ): Either<E, FList<B>> = xs.foldRight(Right(FList())) { a: A, acc: Either<E, FList<B>> ->
        f(a).map2(acc) { a1: B, b2: FList<B> ->
            FList(a1, b2)
        }
    }

    fun <E, A, B> sequence(
        xs: FList<Either<E, A>>,
    ): Either<E, FList<A>> = xs.foldRight(Right(FList())) { a: Either<E, A>, acc: Either<E, FList<A>> ->
        a.map2(acc) { a1: A, b2: FList<A> ->
            FList(a1, b2)
        }
    }
}