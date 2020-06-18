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
            budget = 0,
            collection = null,
            genres = emptyList(),
            hasVideo = false,
            homePage = null,
            imdbId = null,
            movieImage = image,
            movieStatus = MovieStatus.UNKNOWN,
            movieTarget = MovieTarget.ALL_AGE,
            originalLanguage = "",
            originalTitle = "",
            overview = movieItem.overview,
            popularity = 0.0,
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
            revenue = 0,
            runtime = 0,
            spokenLanguages = emptyList(),
            tagline = null,
            title = movieItem.title,
            vote = Vote(0.0, 0),
            reviews = emptyList()
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