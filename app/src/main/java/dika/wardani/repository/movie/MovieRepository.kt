package dika.wardani.repository.movie

import dika.wardani.domain.Movie
import dika.wardani.domain.Page
import dika.wardani.domain.Review
import dika.wardani.util.Result
import io.reactivex.Single

interface MovieRepository {
    fun getTopRatedMovies(pageNumber: Int): Single<Result<Page<Movie>>>
    fun getNowPlayingMovies(pageNumber: Int): Single<Result<Page<Movie>>>
    fun getPopularMovies(pageNumber: Int): Single<Result<Page<Movie>>>
    fun getMovieDetail(movieId: Int): Single<Result<Movie>>
    fun saveFavourite(movie: Movie): Single<Result<Unit>>
}