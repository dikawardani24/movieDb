package dika.wardani.util.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}