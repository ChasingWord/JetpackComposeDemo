package com.shrimp.compose.bean

/**
 * Created by chasing on 2022/3/22.
 */
data class TopicData(
    val id: Int,
    var type: Int = 0,
    var imgList: List<String> = listOf(),
    var isFocus: Boolean = false,
    var isPraise: Boolean = false,
) {
    companion object {
        const val TOPIC_DATA_TYPE_IMAGE = 0
        const val TOPIC_DATA_TYPE_RESOURCE = 1
        const val TOPIC_DATA_TYPE_NEWS = 2
    }
}
