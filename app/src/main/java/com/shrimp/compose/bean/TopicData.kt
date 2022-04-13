package com.shrimp.compose.bean

/**
 * Created by chasing on 2022/3/22.
 */
data class TopicData(
    val type: Int, var isFocus: Boolean,
    val imgList: List<String> = listOf(),
)

const val TOPIC_DATA_TYPE_IMAGE = 0
const val TOPIC_DATA_TYPE_RESOURCE = 1
const val TOPIC_DATA_TYPE_NEWS = 2
