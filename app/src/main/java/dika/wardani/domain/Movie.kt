package dika.wardani.domain

import java.util.*

data class Movie(
    var id: Int,
    var genres: List<Genre>,
    var movieTarget: MovieTarget,
    var movieImage: MovieImage?,
    var collection: Collection?,
    var budget: Int,
    var homePage: String?,
    var imdbId: String?,
    var title: String,
    var originalLanguage: String,
    var overview: String,
    var popularity: Double,
    var productionCompanies: List<ProductionCompany>,
    var productionCountries: List<ProductionCountry>,
    var releaseDate: Date,
    var revenue: Int,
    var runtime: Int?,
    var spokenLanguages: List<SpokenLanguage>,
    var movieStatus: MovieStatus,
    var tagline: String?,
    var originalTitle: String,
    var hasVideo: Boolean,
    var vote: Vote
)