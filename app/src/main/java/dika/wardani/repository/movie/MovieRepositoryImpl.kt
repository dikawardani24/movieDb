package dika.wardani.repository.movie

import android.util.Log
import dika.wardani.api.ApiConfig
import dika.wardani.api.MovieEndPoint
import dika.wardani.api.mapper.MovieMapper
import dika.wardani.domain.Movie
import dika.wardani.domain.Page
import dika.wardani.exception.AllreadyExistException
import dika.wardani.exception.NotFoundException
import dika.wardani.exception.NotMoreDataException
import dika.wardani.exception.SystemException
import dika.wardani.local.dao.FavouriteMovieDao
import dika.wardani.repository.BaseRepository
import dika.wardani.util.Result
import io.reactivex.Single

class MovieRepositoryImpl(
    private val movieEndPoint: MovieEndPoint,
    private val favouriteMovieDao: FavouriteMovieDao
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

    override fun saveFavourite(movie: Movie): Single<Result<Unit>> {
        return Single.create {
            try {
                val foundEntity = favouriteMovieDao.findById(movieId = movie.id)
                if (foundEntity == null) {
                    val entity = dika.wardani.local.mapper.MovieMapper.toEntity(movie)
                    Log.d(TAG, entity.toString())
                    favouriteMovieDao.save(entity)
                    it.onSuccess(Result.Succeed(Unit))
                } else {
                    it.onSuccess(Result.Failed(AllreadyExistException("Movie is already on favourite")))
                }
            } catch (e: Exception) {
                it.onSuccess(Result.Failed(SystemException("Unable to save favourite movie", e)))
            }
        }
    }

    override fun loadFavouriteMovie(pageNumber: Int): Single<Result<Page<Movie>>> {
        return Single.create {
            try {
                val offset = (pageNumber -1) * limitLoadLocalData

                Log.d(TAG, "limit: $limitLoadLocalData, requestPage: $pageNumber, offset: $offset")
                val entities = favouriteMovieDao.findAll(
                    limit = limitLoadLocalData,
                    offset = offset
                )

                if (entities.isNotEmpty()) {
                    val pageMovies = dika.wardani.local.mapper.MovieMapper.toPageMovie(pageNumber, entities)
                    it.onSuccess(Result.Succeed(pageMovies))
                } else {
                    val error = if (pageNumber > 1) {
                        NotMoreDataException("No more data favourite movies")
                    } else {
                        NotFoundException("No data favourite movies")
                    }

                    it.onSuccess(Result.Failed(error))
                }

            } catch (e: Exception) {
                it.onSuccess(Result.Failed(SystemException("Unable to load favourite movies", e)))
            }
        }
    }

    override fun findFavouriteMovie(movieId: Int): Single<Result<Movie>> {
        return Single.create {
            try {
                val foundMovieEntity = favouriteMovieDao.findById(movieId)
                if (foundMovieEntity != null) {
                    val foundMovie = dika.wardani.local.mapper.MovieMapper.toMovie(foundMovieEntity)
                    it.onSuccess(Result.Succeed(foundMovie))
                } else {
                    it.onSuccess(Result.Failed(NotFoundException("No data movie with id : $movieId")))
                }
            } catch (e: Exception) {
                it.onSuccess(Result.Failed(SystemException("Unable to find movie", e)))
            }
        }
    }

    override fun deleteFavourite(movie: Movie): Single<Result<Unit>> {
        return Single.create {
            try {
                val foundMovieEntity = favouriteMovieDao.findById(movieId = movie.id)
                if (foundMovieEntity != null) {
                    favouriteMovieDao.delete(foundMovieEntity)
                    it.onSuccess(Result.Succeed(Unit))
                } else {
                    it.onSuccess(Result.Failed(NotFoundException("No data movie with id : ${movie.id}")))
                }
            } catch (e: Exception) {
                it.onSuccess(Result.Failed(SystemException("Unable to find movie", e)))
            }
        }
    }

    companion object {
        private const val TAG = "MovieRepositoryImpl"
    }
}