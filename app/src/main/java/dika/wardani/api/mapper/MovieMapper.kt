package dika.wardani.api.mapper

import dika.wardani.api.response.MovieDetailResponse
import dika.wardani.api.response.NowPlayingMovieResponse
import dika.wardani.api.response.PopularMovieResponse
import dika.wardani.api.response.TopRatedMovieResponse
import dika.wardani.api.response.model.MovieItem
import dika.wardani.domain.*
import dika.wardani.domain.Collection
import dika.wardani.util.DateFormatterHelper

object MovieMapper {
    const val imageBaseUrl = "https://image.tmdb.org/t/p/w500"

    private fun toMovie(movieItem: MovieItem): Movie {
        val genres = ArrayList<Genre>()

        genres.run {
            movieItem.genreIds.forEach {
                add(Genre(it, ""))
            }
        }

        val image = if (movieItem.backDropPath == null && movieItem.posterPath == null)
            null else MovieImage(
            backDropPath = "$imageBaseUrl${movieItem.backDropPath}",
            posterPath = "$imageBaseUrl${movieItem.posterPath}"
        )


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
            vote = vote,
            reviews = emptyList()
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
            number = topRatedMovieResponse.page,
            datas = toMovieList(topRatedMovieResponse.result)
        )
    }

    fun toMoviePage(nowPlayingMovieResponse: NowPlayingMovieResponse): Page<Movie> {
        return Page(
            number = 0,
            datas = toMovieList(nowPlayingMovieResponse.result)
        )
    }

    fun toMoviePage(popularMovieResponse: PopularMovieResponse): Page<Movie> {
        return Page(
            number = popularMovieResponse.page,
            datas = toMovieList(popularMovieResponse.result)
        )
    }

    fun toMovie(movieItem: MovieDetailResponse): Movie {
        val genres = ArrayList<Genre>()
        genres.run {
            movieItem.genres.forEach {
                add(Genre(it.id, it.name))
            }
        }

        val image = if (movieItem.backdropPath == null && movieItem.posterPath == null)
            null else MovieImage(
            backDropPath = "$imageBaseUrl${movieItem.backdropPath}",
            posterPath = "$imageBaseUrl${movieItem.posterPath}"
        )
        
        val vote = Vote(
            average = movieItem.voteAverage,
            count = movieItem.voteCount
        )

        val collection = if (movieItem.belongsToCollection == null)
            null else Collection(id = movieItem.belongsToCollection.id, name = "")

        val productionCompany = ArrayList<ProductionCompany>()
        movieItem.productionCompanies.forEach {
            val company = ProductionCompany(
                id = it.id,
                name = it.name,
                logoPath = it.logoPath,
                originCountry = it.originCountry
            )
            productionCompany.add(company)
        }

        val productionCountry = ArrayList<ProductionCountry>()
        movieItem.productionCountries.forEach {
            val country = ProductionCountry(
                iso31661 = it.iso31661,
                name = it.name
            )
            productionCountry.add(country)
        }

        val spokenLanguage = ArrayList<SpokenLanguage>()
        movieItem.spokenLanguages.forEach {
            val language = SpokenLanguage(
                iso6391 = it.iso6391,
                name = it.name
            )
            spokenLanguage.add(language)
        }

        return Movie(
            id = movieItem.id,
            budget = movieItem.budget,
            collection = collection,
            genres = genres,
            hasVideo = movieItem.video,
            homePage = movieItem.homepage,
            imdbId = movieItem.imdbId,
            movieImage = image,
            movieStatus = MovieStatus.UNKNOWN,
            movieTarget = if (movieItem.adult) MovieTarget.ADULT else MovieTarget.ALL_AGE,
            originalLanguage = movieItem.originalLanguage,
            originalTitle = movieItem.originalTitle,
            overview = movieItem.overview ?: "",
            popularity = movieItem.popularity,
            productionCompanies = productionCompany,
            productionCountries = productionCountry,
            releaseDate = DateFormatterHelper.toDateInstance(movieItem.releaseDate),
            revenue = movieItem.revenue,
            runtime = movieItem.runtime,
            spokenLanguages = spokenLanguage,
            tagline = movieItem.tagline,
            title = movieItem.title,
            vote = vote,
            reviews = emptyList()
        )
    }
}