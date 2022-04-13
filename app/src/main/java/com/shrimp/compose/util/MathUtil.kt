package com.shrimp.compose.util

/**
 * Created by chasing on 2022/3/23.
 */
fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}