package dika.wardani.activity.movieList

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

class MovieListViewModel(
    application: Application,
    private val movieRepository: MovieRepository
) : AndroidViewModel(application) {
    private var currentProcess: Disposable? = null
    private var currentPage: Int = 1
    var currentFilterType: FilterType = FilterType.TOP_RATED

    fun resetPage() {
        currentPage = 1
    }

    fun stop() {
        currentProcess?.dispose()
    }

    fun getMovies(): LiveData<Result<List<Movie>>> {
        val liveData = MutableLiveData<Result<List<Movie>>>()

        Log.d(TAG, "page: $currentPage, filter: $currentFilterType")

        val single = when(currentFilterType) {
            FilterType.TOP_RATED -> movieRepository.getTopRatedMovies(currentPage)
            FilterType.NOW_PLAYING -> movieRepository.getNowPlayingMovies(currentPage)
            FilterType.POPULAR -> movieRepository.getPopularMovies(currentPage)
        }

        currentProcess = single.subscribeOn(Schedulers.io())
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

        return liveData
    }

    enum class FilterType {
        TOP_RATED,
        POPULAR,
        NOW_PLAYING
    }

    companion object {
        private const val TAG = "MovieListViewModel"
    }
}