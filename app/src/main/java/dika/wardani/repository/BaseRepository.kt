package dika.wardani.repository

import com.google.gson.Gson
import dika.wardani.api.response.ErrorResponse
import dika.wardani.exception.NotFoundException
import dika.wardani.exception.SystemException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseRepository {

    private fun handle404Error(httpException: HttpException): Throwable {
        val responseBody = httpException.response()?.errorBody()?.string()

        return if (responseBody != null) {
            val errorResponse = Gson().fromJson(responseBody, ErrorResponse::class.java)
            if (errorResponse.statusCode == 34) {
                NotFoundException("Detail movie not found, body : $responseBody", httpException)
            } else {
                SystemException("Message: ${errorResponse.statusMessage}, status code: ${errorResponse.statusCode}")
            }
        } else {
            SystemException("${httpException.message}")
        }
    }

    protected fun handle(throwable: Throwable): Throwable {
        return when (throwable) {
            is SocketTimeoutException -> SystemException("Request timeout", throwable)
            is ConnectException -> SystemException("Unable to connect to server", throwable)
            is HttpException -> {
                when (throwable.code()) {
                    404 -> handle404Error(throwable)
                    else -> SystemException("Unknown HTTP error, response: ${throwable.response().toString()}")
                }
            }
            else -> SystemException(
                "Unknown error occurred, exception type is ${throwable::class} and message is ${throwable.message}",
                throwable
            )
        }
    }
}