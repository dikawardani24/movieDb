package dika.wardani.api.mapper

import dika.wardani.api.response.MovieReviewResponse
import dika.wardani.api.response.model.ReviewItem
import dika.wardani.domain.Movie
import dika.wardani.domain.Page
import dika.wardani.domain.Review

object ReviewMapper {
    
    private fun toReview(movie: Movie, reviewItem: ReviewItem): Review {
        return Review(
            id = reviewItem.id,
            author = reviewItem.author,
            content = reviewItem.content,
            movie = movie,
            url = reviewItem.url
        )
    }
    
    private fun toReviews(movie: Movie, reviewItems: List<ReviewItem>): List<Review> {
        val reviews = ArrayList<Review>()
        reviewItems.forEach { 
            reviews.add(toReview(movie, it))
        }
        return reviews
    }
    
    fun toReviewPage(movie: Movie, movieReviewResponse: MovieReviewResponse): Page<Review> {
        return Page(
            page = movieReviewResponse.page,
            datas = toReviews(movie, movieReviewResponse.reviews)
        )
    }
}