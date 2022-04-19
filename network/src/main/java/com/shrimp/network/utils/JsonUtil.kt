package com.shrimp.network.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by chasing on 2021/11/11.
 */
object JsonUtil {

    val gson = Gson()

    inline fun <reified T> fromJson(jsonString: String): T? {
        return try {
            gson.fromJson(jsonString, T::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun <T> fromJsonList(jsonString: String, token: TypeToken<List<T>>): List<T>? {
        return try {
            gson.fromJson(jsonString, token.type)
        } catch (e: Exception) {
            null
        }
    }

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }
}