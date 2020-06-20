package dika.wardani.activity.favouriteMovie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dika.wardani.domain.Movie
import dika.wardani.repository.movie.MovieRepository
import dika.wardani.util.Result
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavouriteMoviesViewModel(
    application: Application,
    private val movieRepository: MovieRepository
): AndroidViewModel(application) {
    private var currentProcess: Disposable? = null
    private var currentPage: Int = 1

    fun stop() {
        val process = currentProcess
        if (process != null && !process.isDisposed) {
            process.dispose()
        }
    }

    fun loadFavouriteViewModel(): LiveData<Result<List<Movie>>> {
        val liveData = MutableLiveData<Result<List<Movie>>>()

        currentProcess = movieRepository.loadFavouriteMovie(currentPage)
            .subscribeOn(Schedulers.io())
            .doAfterSuccess {
                Log.d(TAG, "$it")
                when(it) {
                    is Result.Succeed -> {
                       val page = it.data
                        currentPage += 1
                        liveData.postValue(Result.Succeed(page.datas))
                    }

                    is Result.Failed -> {
                        Log.d(TAG, "${it.error.message}")
                        liveData.postValue(Result.Failed(it.error))
                    }

                }
            }
            .subscribe()

        return liveData
    }

    companion object {
        private const val TAG = "FavMoviesViewModel"
    }
}