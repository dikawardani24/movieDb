package dika.wardani.util

sealed class Result<T> {
    data class Succeed<T>(val data: T): Result<T>()
    data class Failed<T>(val error: Throwable): Result<T>()
}