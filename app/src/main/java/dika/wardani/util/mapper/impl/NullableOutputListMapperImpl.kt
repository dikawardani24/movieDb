package dika.wardani.util.mapper.impl

import dika.wardani.util.mapper.Mapper
import dika.wardani.util.mapper.NullableOutputListMapper

class NullableOutputListMapperImpl<I, O>(private val mapper: Mapper<I, O>):
    NullableOutputListMapper<I, O> {
    override fun map(input: List<I>): List<O>? {
        return if (input.isEmpty()) {
            null
        } else {
            ArrayList<O>().apply {
                input.forEach {
                    add(mapper.map(it))
                }
            }
        }
    }
}