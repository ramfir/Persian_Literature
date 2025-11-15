package com.firdavs.persianliterature.core.mapper

fun interface Mapper<INPUT, OUTPUT> {
    fun map(input: INPUT): OUTPUT
}
