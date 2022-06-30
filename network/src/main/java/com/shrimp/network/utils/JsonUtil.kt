package com.shrimp.network.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by chasing on 2021/11/11.
 */
val gson = Gson()
fun Any.toJsonString(): String {
    return gson.toJson(this)
}

fun <T> String.toJsonObject(token: TypeToken<List<T>>): List<T>? {
    return try {
        gson.fromJson(this, token.type)
    } catch (e: Exception) {
        null
    }
}