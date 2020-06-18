package dika.wardani.repository.review

import dika.wardani.domain.Movie
import dika.wardani.domain.Page
import dika.wardani.domain.Review
import dika.wardani.util.Result
import io.reactivex.Single

interface ReviewRepository {
    fun getMovieReviews(movie: Movie, pageNumber: Int): Single<Result<Page<Review>>>
}