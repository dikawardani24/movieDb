package dika.wardani.util.mapper.impl

import dika.wardani.util.mapper.Mapper
import dika.wardani.util.mapper.NullableInputListMapper

class NullableInputListMapperImpl<I, O>(private val mapper: Mapper<I, O>):
    NullableInputListMapper<I, O> {
    override fun map(input: List<I>?): List<O> {
        return if (input != null) {
            ArrayList<O>().apply {
                input.forEach {
                    add(mapper.map(it))
                }
            }
        } else {
            emptyList()
        }
    }
}