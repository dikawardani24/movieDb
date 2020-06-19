package dika.wardani.local.mapper

import dika.wardani.api.mapper.MovieMapper
import dika.wardani.domain.*
import dika.wardani.local.entity.FavouriteMovieEntity
import dika.wardani.util.DateFormatterHelper

object MovieMapper {

    fun toEntity(movie: Movie): FavouriteMovieEntity {
        return FavouriteMovieEntity(
            movieId = movie.id,
            title = movie.title,
            releaseDate = DateFormatterHelper.format(movie.releaseDate),
            overview = movie.overview,
            backDropPath = movie.movieImage?.backDropPath,
            posterPath = movie.movieImage?.posterPath
        )
    }

    private fun toMovie(movieItem: FavouriteMovieEntity): Movie {
        val image = if (movieItem.backDropPath == null && movieItem.posterPath == null)
            null else MovieImage(
            backDropPath = "${MovieMapper.imageBaseUrl}${movieItem.backDropPath}",
            posterPath = "${MovieMapper.imageBaseUrl}${movieItem.posterPath}"
        )

        return Movie(
            id = movieItem.movieId,
            movieImage = image,
            movieStatus = MovieStatus.UNKNOWN,
            movieTarget = MovieTarget.ALL_AGE,
            overview = movieItem.overview,
            releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
            title = movieItem.title
        )
    }

    private fun toMovies(favouriteMovieEntities: List<FavouriteMovieEntity>): List<Movie> {
        val movies = ArrayList<Movie>()

        favouriteMovieEntities.forEach {
            movies.add(toMovie(it))
        }

        return movies
    }

    fun toPageMovie(pageNumber: Int, favouriteMovieEntities: List<FavouriteMovieEntity>): Page<Movie> {
        return Page(
            number = pageNumber,
            datas = toMovies(favouriteMovieEntities)
        )
    }
}