package dika.wardani.repository

import dika.wardani.exception.SystemException
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseRepository {

    protected fun handle(throwable: Throwable): Throwable {
        return when (throwable) {
            is SocketTimeoutException -> SystemException("Request timeout", throwable)
            is ConnectException -> SystemException("Unable to connect to server", throwable)
            else -> SystemException("Unknown error occurred, exception type is ${throwable::class} and message is ${throwable.message}", throwable)
        }
    }

}