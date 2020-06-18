package dika.wardani.repository.review

import dika.wardani.api.ApiConfig
import dika.wardani.api.ReviewEndPoint
import dika.wardani.api.mapper.ReviewMapper
import dika.wardani.domain.Movie
import dika.wardani.domain.Page
import dika.wardani.domain.Review
import dika.wardani.exception.SystemException
import dika.wardani.repository.BaseRepository
import dika.wardani.util.Result
import io.reactivex.Single

class ReviewRepositoryImpl(
    private val reviewEndPoint: ReviewEndPoint
): BaseRepository(), ReviewRepository {

    override fun getMovieReviews(movie: Movie, pageNumber: Int): Single<Result<Page<Review>>> {
        return reviewEndPoint.getMovieReviews(
            movieId = movie.id,
            apiKey = ApiConfig.API_KEY,
            language = ApiConfig.LANGUANGE,
            page = pageNumber
        ).map {
            val totalFetchedData = it.reviews.size
            if (totalFetchedData > 0) {
                val page = ReviewMapper.toReviewPage(movie = movie, movieReviewResponse = it)
                Result.Succeed(page)
            } else {
                Result.Failed(SystemException("No more data"))
            }

        }
    }
}