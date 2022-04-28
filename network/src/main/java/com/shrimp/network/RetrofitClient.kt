package com.shrimp.network

import com.shrimp.base.BuildConfig
import com.shrimp.network.utils.EncryptionInterceptor
import com.shrimp.network.utils.JsonConverterFactory
import com.shrimp.network.utils.LoggerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by chasing on 2021/10/21.
 */
object RetrofitClient {
    private const val IP = "https://www.huixueba.net/"

    /**
     * 请求超时时间
     */
    private const val DEFAULT_TIMEOUT = 30000
    private val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder().run {
                readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                addInterceptor(EncryptionInterceptor())
                if (BuildConfig.DEBUG)
                    addInterceptor(LoggerInterceptor())
                build()
            }
        }

    private lateinit var retrofit: Retrofit

    val _retrofit: Retrofit
        get() {
            if (!RetrofitClient::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .baseUrl(IP)
                    .client(okHttpClient)
                    .addConverterFactory(JsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}