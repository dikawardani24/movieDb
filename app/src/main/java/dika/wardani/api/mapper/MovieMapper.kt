package dika.wardani.api.mapper

import dika.wardani.api.response.NowPlayingMovieResponse
import dika.wardani.api.response.PopularMovieResponse
import dika.wardani.api.response.TopRatedMovieResponse
import dika.wardani.api.response.model.MovieItem
import dika.wardani.domain.*
import dika.wardani.util.DateFormatterHelper

object MovieMapper {

    private fun toMovie(movieItem: MovieItem): Movie {
        val genres = ArrayList<Genre>()

        genres.run {
            movieItem.genreIds.forEach {
                add(Genre(it.toLong(), ""))
            }
        }

        val image = if (movieItem.backDropPath == null && movieItem.posterPath == null)
            null else MovieImage(backDropPath = movieItem.backDropPath, posterPath = movieItem.posterPath)


        val vote = Vote(
            average = movieItem.voteAverage,
            count = movieItem.voteCount
        )

        return Movie(
            id = movieItem.id,
            budget = 0,
            collection = null,
            genres = genres,
            hasVideo = movieItem.video,
            homePage = null,
            imdbId = null,
            movieImage = image,
            movieStatus = MovieStatus.UNKNOWN,
            movieTarget = if (movieItem.adult) MovieTarget.ADULT else MovieTarget.ALL_AGE,
            originalLanguage = movieItem.originalLanguage,
            originalTitle = movieItem.originalTitle,
            overview = movieItem.overview,
            popularity = movieItem.popularity,
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
            revenue = 0,
            runtime = 0,
            spokenLanguages = emptyList(),
            tagline = null,
            title = movieItem.title,
            vote = vote
        )
    }

    private fun toMovieList(movieItems: List<MovieItem>): List<Movie> {
        val list = ArrayList<Movie>()

        movieItems.forEach {
            list.add(toMovie(it))
        }

        return list
    }

    fun toMoviePage(topRatedMovieResponse: TopRatedMovieResponse): Page<Movie> {

        return  Page(
            page = topRatedMovieResponse.page,
            datas = toMovieList(topRatedMovieResponse.result)
        )
    }

    fun toMoviePage(nowPlayingMovieResponse: NowPlayingMovieResponse): Page<Movie> {
        return Page(
            page = 0,
            datas = toMovieList(nowPlayingMovieResponse.result)
        )
    }

    fun toMoviePage(popularMovieResponse: PopularMovieResponse): Page<Movie> {
        return Page(
            page = popularMovieResponse.page,
            datas = toMovieList(popularMovieResponse.result)
        )
    }
}