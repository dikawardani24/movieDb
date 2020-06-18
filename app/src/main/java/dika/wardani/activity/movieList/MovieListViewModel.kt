package dika.wardani.activity.movieList

import android.app.Application
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
    private var currentPage = 1
    private var currentProsess: Disposable? = null

    fun stop() {
        currentProsess?.dispose()
    }

    fun loadTopRatedMovie(): LiveData<Result<List<Movie>>> {
        val liveData = MutableLiveData<Result<List<Movie>>>()

        currentProsess = movieRepository.getTopRatedMovies(currentPage)
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

        return liveData
    }
}