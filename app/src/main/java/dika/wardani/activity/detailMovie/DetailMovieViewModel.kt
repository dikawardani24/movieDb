package dika.wardani.activity.detailMovie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dika.wardani.domain.Movie
import dika.wardani.domain.Review
import dika.wardani.exception.SystemException
import dika.wardani.repository.movie.MovieRepository
import dika.wardani.repository.review.ReviewRepository
import dika.wardani.util.Result
import io.reactivex.schedulers.Schedulers

class DetailMovieViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
): AndroidViewModel(application) {
    private var currentMovie: Movie? = null

    fun saveMoveAsFavourite(): LiveData<Result<Unit>> {
        val liveData = MutableLiveData<Result<Unit>>()

        val movieToSave = currentMovie
        if (movieToSave != null) {
            movieRepository.saveFavourite(movieToSave)
                .subscribeOn(Schedulers.io())
                .doAfterSuccess {
                    when(it) {
                        is Result.Succeed -> liveData.postValue(Result.Succeed(Unit))
                        is Result.Failed -> liveData.postValue(Result.Failed(it.error))
                    }
                }
                .subscribe()
        } else {
            liveData.postValue(Result.Failed(SystemException("No data movie")))
        }
        return liveData
    }
    fun loadReviews(movie: Movie): LiveData<Result<List<Review>>> {
        val liveData = MutableLiveData<Result<List<Review>>>()

        reviewRepository.getMovieReviews(movie, 1)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val page = it.data
                        liveData.postValue(Result.Succeed(page.datas))
                    }
                    is Result.Failed -> {
                        liveData.postValue(Result.Failed(it.error))
                    }
                }
            }
            .subscribe()

        return liveData
    }

    fun loadDetailMovie(movieId: Int): LiveData<Result<Movie>> {
        val liveData = MutableLiveData<Result<Movie>>()

        movieRepository.getMovieDetail(movieId)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val movie = it.data
                        currentMovie = movie
                        liveData.postValue(Result.Succeed(movie))
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