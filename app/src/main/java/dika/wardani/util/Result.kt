package dika.wardani.util

import dika.wardani.exception.SystemException

abstract class Result<T> {
    data class Succeed<T>(val data: T): Result<T>()
    data class Failed<T>(val error: SystemException): Result<T>()
}