package dika.wardani.util.mapper

import dika.wardani.util.mapper.impl.ListMapperImpl

interface ListMapper<I, O> : Mapper<List<I>, List<O>> {

    companion object {
        fun <I, O> createFrom(mapper: Mapper<I, O>): ListMapper<I, O> {
            return ListMapperImpl(mapper)
        }
    }

}

