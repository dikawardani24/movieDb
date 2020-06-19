package dika.wardani.domain

import java.util.*

data class Movie(
    var id: Int = 0,
    var genres: List<Genre> = emptyList(),
    var movieTarget: MovieTarget = MovieTarget.ALL_AGE,
    var movieImage: MovieImage? = null,
    var collection: Collection? = null,
    var budget: Int = 0,
    var homePage: String? = null,
    var imdbId: String? = null,
    var title: String = "",
    var originalLanguage: String = "",
    var overview: String = "",
    var popularity: Double = 0.0,
    var productionCompanies: List<ProductionCompany> = emptyList(),
    var productionCountries: List<ProductionCountry> = emptyList(),
    var releaseDate: Date = Calendar.getInstance().time,
    var revenue: Int = 0,
    var runtime: Int? = null,
    var spokenLanguages: List<SpokenLanguage> = emptyList(),
    var movieStatus: MovieStatus = MovieStatus.UNKNOWN,
    var tagline: String? = null,
    var originalTitle: String = "",
    var hasVideo: Boolean = false,
    var vote: Vote = Vote()
)