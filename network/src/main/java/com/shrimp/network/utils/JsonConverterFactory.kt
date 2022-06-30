package com.shrimp.network.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by chasing on 2021/10/21.
 */
class JsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    companion object {
        fun create(): JsonConverterFactory {
            return JsonConverterFactory(Gson())
        }
    }

    // type对应的是RequestBody的类型
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    // type对应的是Deffer<T>里面的T类型
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *> {
        return JsonResponseBodyConverter(gson.getAdapter(TypeToken.get(type)))
    }
}