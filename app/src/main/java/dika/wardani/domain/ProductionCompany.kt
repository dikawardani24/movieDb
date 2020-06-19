package dika.wardani.domain

data class ProductionCompany(
    var id: Int = 0,
    var name: String = "",
    var logoPath: String? = null,
    var originCountry: String = ""
)