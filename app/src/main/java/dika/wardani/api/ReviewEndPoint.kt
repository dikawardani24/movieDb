package dika.wardani.api

import dika.wardani.api.response.MovieReviewResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewEndPoint {
    @GET("{movieId}/reviews")
    fun getMovieReviews(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Single<MovieReviewResponse>
}