package dika.wardani.domain

data class Page<T>(
    var page: Number,
    var datas: List<T>
)