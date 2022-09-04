package com.connect.domain.utils

fun Int.getPositive(): Int {
    return if (this < 0) return this.times(-1) else this
}