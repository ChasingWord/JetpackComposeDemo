package com.shrimp.network

import com.shrimp.network.api.ExampleApi
import com.shrimp.network.entity.base.ResponseResult
import com.shrimp.network.entity.res.Tags

/**
 * Created by chasing on 2021/10/21.
 */
object RequestManager {

    private val mApi: ExampleApi = RetrofitClient._retrofit.create(ExampleApi::class.java)

    suspend fun getCourseTagsMenuByUserId(
        checkCode: String,
        userId: Int,
    ): ResponseResult<List<Tags>> = mApi.getCourseTagsMenuByUserId(checkCode, userId)
}