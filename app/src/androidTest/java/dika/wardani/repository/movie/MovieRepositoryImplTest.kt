package dika.wardani.repository.movie

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import dika.wardani.exception.NotFoundException
import dika.wardani.exception.SystemException
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.Result
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieRepositoryImplTest {
    private val TAG = "MovieRepositoryImplTest"
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getTopRatedMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getTopRatedMovies(1).observeOn(Schedulers.io())
            .subscribe { result ->
                when(result) {
                    is Result.Succeed -> {
                        val data = result.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(data.datas.isNotEmpty())
                    }
                    is Result.Failed -> {
                        throw result.error
                    }
                }
            }
    }

    @Test
    fun getNowPlayingMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getNowPlayingMovies(1).observeOn(Schedulers.io())
            .subscribe { result ->
                when(result) {
                    is Result.Succeed -> {
                        val data = result.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(data.datas.isNotEmpty())
                    }
                    is Result.Failed -> {
                        throw result.error
                    }
                }
            }
    }

    @Test
    fun getPopularMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getPopularMovies(1).observeOn(Schedulers.io())
            .subscribe { result ->
                when(result) {
                    is Result.Succeed -> {
                        val data = result.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(data.datas.isNotEmpty())
                    }
                    is Result.Failed -> {
                        throw result.error
                    }
                }
            }
    }

    @Test
    fun getMovieDetail() {
        val repository = RepositoryFactory.getMovieRepository(context)
        repository.getMovieDetail(1).observeOn(Schedulers.io())
            .doOnError {
                assert(it is NotFoundException)
            }
            .subscribe { result ->
                when(result) {
                    is Result.Succeed -> {
                        val data = result.data
                        Log.d(TAG, Gson().toJson(data))
                        assert(true)
                    }
                    is Result.Failed -> {
                        Log.d(TAG, "${result.error.message}")
                    }
                }
            }
    }
}