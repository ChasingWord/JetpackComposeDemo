package com.shrimp.network.api

import com.shrimp.network.entity.base.ResponseResult
import com.shrimp.network.entity.res.Tags
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by chasing on 2021/10/21.
 */
interface ExampleApi {

    @POST("app/course/GetCourseTagsMenuByUserId")
    suspend fun getCourseTagsMenuByUserId(
        @Header("code") code: String,
        @Query("currentUserId") userId: Int,
    ): ResponseResult<List<Tags>>

}
