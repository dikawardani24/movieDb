package dika.wardani.repository.movie

import dika.wardani.api.ApiConfig
import dika.wardani.api.MovieEndPoint
import dika.wardani.api.mapper.MovieMapper
import dika.wardani.domain.Movie
import dika.wardani.domain.Page
import dika.wardani.exception.NotMoreDataException
import dika.wardani.exception.SystemException
import dika.wardani.repository.BaseRepository
import dika.wardani.util.Result
import io.reactivex.Single

class MovieRepositoryImpl(
    private val movieEndPoint: MovieEndPoint
) : BaseRepository(), MovieRepository {

    override fun getTopRatedMovies(pageNumber: Int): Single<Result<Page<Movie>>> {
        return movieEndPoint.getTopRatedMovies(
            page = pageNumber,
            apiKey = ApiConfig.API_KEY,
            language = ApiConfig.LANGUANGE
        ).map {
            val totalFetched = it.result.size
            if (totalFetched > 0) {
                val page = MovieMapper.toMoviePage(it)
                Result.Succeed(page)
            } else {
                Result.Failed<Page<Movie>>(NotMoreDataException("No more data"))
            }
        }.onErrorReturn {
            Result.Failed(handle(it))
        }
    }

    override fun getNowPlayingMovies(pageNumber: Int): Single<Result<Page<Movie>>> {
        return movieEndPoint.getNowPlayingMovies(
            page = pageNumber,
            apiKey = ApiConfig.API_KEY,
            language = ApiConfig.LANGUANGE
        ).map {
            val totalFetched = it.result.size
            if (totalFetched > 0) {
                val page = MovieMapper.toMoviePage(it)
                Result.Succeed(page)
            } else {
                Result.Failed<Page<Movie>>(NotMoreDataException("No more data"))
            }
        }.onErrorReturn {
            Result.Failed(handle(it))
        }
    }

    override fun getPopularMovies(pageNumber: Int): Single<Result<Page<Movie>>> {
        return movieEndPoint.getPopularMovies(
            page = pageNumber,
            apiKey = ApiConfig.API_KEY,
            language = ApiConfig.LANGUANGE
        ).map {
            val totalFetched = it.result.size
            if (totalFetched > 0) {
                val page = MovieMapper.toMoviePage(it)
                Result.Succeed(page)
            } else {
                Result.Failed<Page<Movie>>(NotMoreDataException("No more data"))
            }
        }.onErrorReturn {
            Result.Failed(handle(it))
        }
    }

    override fun getMovieDetail(movieId: Int): Single<Result<Movie>> {
        return movieEndPoint.getDetailMovie(
            movieId = movieId,
            language = ApiConfig.LANGUANGE,
            apiKey = ApiConfig.API_KEY
        ).map {
            val foundMovie = MovieMapper.toMovie(it)
            val result: Result<Movie> = Result.Succeed(foundMovie)
            result
        }.onErrorReturn {
            Result.Failed(handle(it))
        }
    }
}