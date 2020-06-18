package dika.wardani.repository.movie

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.Result
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieRepositoryImplTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getTopRatedMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getTopRatedMovies(1).observeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val data = it.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(data.datas.isNotEmpty())
                    }
                    is Result.Failed -> {
                        Log.d(TAG, "${it.error.message}")
                        throw it.error
                    }
                }
            }.subscribe()
    }

    @Test
    fun getNowPlayingMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getNowPlayingMovies(1).observeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val data = it.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(data.datas.isNotEmpty())
                    }
                    is Result.Failed -> {
                        Log.d(TAG, "${it.error.message}")
                        throw it.error
                    }
                }
            }
    }

    @Test
    fun getPopularMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getPopularMovies(1).observeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val data = it.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(data.datas.isNotEmpty())
                    }
                    is Result.Failed -> {
                        Log.d(TAG, "${it.error.message}")
                        throw it.error
                    }
                }
            }
    }

    @Test
    fun getMovieDetail() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getMovieDetail(514847).observeOn(Schedulers.io())
            .doAfterSuccess {
                when(it) {
                    is Result.Succeed -> {
                        val data = it.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(true)
                    }
                    is Result.Failed -> {
                        Log.d(TAG, "${it.error.message}")
                        throw it.error
                    }
                }
            }.subscribe()
    }

    companion object {
        private const val TAG = "MovieRepositoryImplTest"
    }
}