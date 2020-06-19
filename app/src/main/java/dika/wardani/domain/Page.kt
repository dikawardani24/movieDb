package dika.wardani.domain

data class Page<T>(
    var number: Int = 0,
    var datas: List<T> = emptyList()
)