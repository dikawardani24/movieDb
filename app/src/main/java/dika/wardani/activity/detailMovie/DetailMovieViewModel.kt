package dika.wardani.activity.detailMovie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dika.wardani.domain.Movie
import dika.wardani.domain.Review
import dika.wardani.exception.NotFoundException
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
    var isFavourite: Boolean = false
    private var currentPage: Int = 1

    fun determineFavouriteMovie(): LiveData<Result<Boolean>> {
        val liveData = MutableLiveData<Result<Boolean>>()

        val movie = currentMovie
        if (movie != null) {
            movieRepository.findFavouriteMovie(movieId = movie.id)
                .subscribeOn(Schedulers.io())
                .doAfterSuccess {
                    when(it) {
                        is Result.Succeed -> {
                            isFavourite = true
                            liveData.postValue(Result.Succeed(isFavourite))
                        }
                        is Result.Failed -> {
                            val error = it.error
                            if (error is NotFoundException) {
                                isFavourite = false
                                liveData.postValue(Result.Succeed(isFavourite))
                            } else {
                                liveData.postValue(Result.Failed(error))
                            }
                        }
                    }
                }
                .subscribe()

        } else {
            liveData.postValue(Result.Failed(SystemException("No data movie")))
        }

        return liveData
    }

    private fun markMovieAsFavourite(movieToSave: Movie, liveData: MutableLiveData<Result<String>>) {
        movieRepository.saveFavourite(movieToSave)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                Log.d(TAG, "$it")
                when(it) {
                    is Result.Succeed -> {
                        isFavourite = true
                        liveData.postValue(Result.Succeed("Movie has been marked from favourite list"))
                    }
                    is Result.Failed -> liveData.postValue(Result.Failed(it.error))
                }
            }
            .subscribe()
    }

    private fun removeMarkMovieAsFavourite(movieToDelete: Movie, liveData: MutableLiveData<Result<String>>) {
        movieRepository.deleteFavourite(movieToDelete)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                Log.d(TAG, "$it")
                when(it) {
                    is Result.Succeed -> {
                        isFavourite = false
                        liveData.postValue(Result.Succeed("Movie has been removed as favourite"))
                    }
                    is Result.Failed -> liveData.postValue(Result.Failed(it.error))
                }
            }
            .subscribe()
    }

    fun changeMovieAsFavourite(): LiveData<Result<String>> {
        val liveData = MutableLiveData<Result<String>>()

        val movieToSave = currentMovie
        if (movieToSave != null) {
            if (isFavourite) {
                removeMarkMovieAsFavourite(movieToSave, liveData)
            } else {
                markMovieAsFavourite(movieToSave, liveData)
            }
        } else {
            liveData.postValue(Result.Failed(SystemException("No data movie")))
        }

        return liveData
    }

    fun loadReviews(): LiveData<Result<List<Review>>> {
        val liveData = MutableLiveData<Result<List<Review>>>()

        val movie = currentMovie
        if (movie != null) {
            reviewRepository.getMovieReviews(movie, currentPage)
                .subscribeOn(Schedulers.io())
                .doAfterSuccess {
                    when(it) {
                        is Result.Succeed -> {
                            val page = it.data
                            currentPage += 1
                            liveData.postValue(Result.Succeed(page.datas))
                        }
                        is Result.Failed -> {
                            liveData.postValue(Result.Failed(it.error))
                        }
                    }
                }
                .subscribe()
        } else {
            liveData.postValue(Result.Failed(SystemException("No data movie")))
        }

        return liveData
    }

    fun loadDetailMovie(movieId: Int): LiveData<Result<Movie>> {
        val liveData = MutableLiveData<Result<Movie>>()

        movieRepository.getMovieDetail(movieId)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                Log.d(TAG, "$it")
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

    companion object {
        private const val TAG = "DetailMovieViewModel"
    }
}