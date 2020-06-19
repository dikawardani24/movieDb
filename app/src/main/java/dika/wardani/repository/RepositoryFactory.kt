package dika.wardani.repository

import android.content.Context
import dika.wardani.api.ApiClient
import dika.wardani.api.MovieEndPoint
import dika.wardani.api.ReviewEndPoint
import dika.wardani.local.AppDatabase
import dika.wardani.repository.movie.MovieRepository
import dika.wardani.repository.movie.MovieRepositoryImpl
import dika.wardani.repository.review.ReviewRepository
import dika.wardani.repository.review.ReviewRepositoryImpl

object RepositoryFactory {

    fun getMovieRepository(context: Context): MovieRepository {
        val endpoint = ApiClient.createEndPoint(
            context = context,
            apiInterfaceClass = MovieEndPoint::class.java
        )

        return MovieRepositoryImpl(
            movieEndPoint = endpoint,
            favouriteMovieDao = AppDatabase.getInstance(context).favouriteMovieDao
        )
    }

    fun getReviewRepository(context: Context): ReviewRepository {
        val endPoint = ApiClient.createEndPoint(
            context = context,
            apiInterfaceClass = ReviewEndPoint::class.java
        )

        return ReviewRepositoryImpl(
            reviewEndPoint = endPoint
        )
    }
}