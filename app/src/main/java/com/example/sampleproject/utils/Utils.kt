package com.example.sampleproject.utils

import kotlin.random.Random

fun generateRandomColorList(size: Int): List<Int> {
    return List(size) {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        (red shl 16) or (green shl 8) or blue
    }
}