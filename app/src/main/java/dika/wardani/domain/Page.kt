package dika.wardani.domain

data class Page<T>(
    var number: Int,
    var datas: List<T>
)