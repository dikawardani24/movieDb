package dika.wardani.util.mapper.impl

import dika.wardani.util.mapper.ListMapper
import dika.wardani.util.mapper.Mapper

class ListMapperImpl<I, O>(private val mapper: Mapper<I, O>) : ListMapper<I, O> {

    override fun map(input: List<I>): List<O> {
        return ArrayList<O>().apply {
            input.forEach {
                add(mapper.map(it))
            }
        }
    }
}