package dika.wardani.repository.movie

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.Result
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieRepositoryImplTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getTopRatedMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        val result = repository.getTopRatedMovies(1).observeOn(Schedulers.io())
            .blockingGet()

        Assert.assertTrue(result is Result.Succeed)
    }

    @Test
    fun getNowPlayingMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        val result = repository.getNowPlayingMovies(1).observeOn(Schedulers.io())
            .blockingGet()

        Assert.assertTrue(result is Result.Succeed)
    }

    @Test
    fun getPopularMovies() {
        val repository = RepositoryFactory.getMovieRepository(context)
        val result = repository.getPopularMovies(1)
            .observeOn(Schedulers.io())
            .blockingGet()

        Assert.assertTrue(result is Result.Succeed)
    }

    @Test
    fun getMovieDetail() {
        val notExistMovieId = 1
        val existMovieId  = 514847

        val repository = RepositoryFactory.getMovieRepository(context)
        val it = repository.getMovieDetail(existMovieId)
            .observeOn(Schedulers.io())
            .blockingGet()

        Assert.assertTrue(it is Result.Succeed)
    }

    companion object {
        private const val TAG = "MovieRepositoryImplTest"
    }
}