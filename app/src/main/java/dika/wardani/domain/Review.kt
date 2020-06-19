package dika.wardani.domain

data class Review(
    var id: String = "",
    var author: String = "",
    var content: String = "",
    var url: String = "",
    var movie: Movie = Movie()
)