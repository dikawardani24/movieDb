package dika.wardani.domain

data class ProductionCompany(
    var id: Long,
    var name: String,
    var logoPath: String?,
    var originCountry: String
)