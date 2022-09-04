package com.connect.framework.remote.utils

import com.connect.domain.Mapper

fun <T, R> List<T>.mapTo(mapper: Mapper<T, R>): List<R> {
  return map { mapper.map(it) }
}

fun <T, R> Mapper<T, R>.toList(): Mapper<List<T>, List<R>> {

  return object : Mapper<List<T>, List<R>> {
    override fun map(input: List<T>): List<R> {
      return input.map {
        map(it)
      }
    }
  }
}