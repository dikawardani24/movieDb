package dika.wardani.activity.detailMovie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dika.wardani.domain.Movie
import dika.wardani.repository.movie.MovieRepository
import dika.wardani.repository.review.ReviewRepository
import dika.wardani.util.Result
import io.reactivex.schedulers.Schedulers

class DetailMovieViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
): AndroidViewModel(application) {

    private fun loadReviews(liveData: MutableLiveData<Result<Movie>>, movie: Movie) {
        reviewRepository.getMovieReviews(movie, 1)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val reviews = it.data
                        movie.reviews = reviews.datas
                        liveData.postValue(Result.Succeed(movie))
                    }
                    is Result.Failed -> {
                        liveData.postValue(Result.Failed(it.error))
                    }
                }
            }
            .subscribe()
    }

    fun loadDetailMovie(movieId: Int): LiveData<Result<Movie>> {
        val liveData = MutableLiveData<Result<Movie>>()

        movieRepository.getMovieDetail(movieId)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val movie = it.data
                        loadReviews(liveData, movie)
                    }
                    is Result.Failed -> {
                        liveData.postValue(Result.Failed(it.error))
                    }
                }
            }
            .subscribe()

        return liveData
    }
}