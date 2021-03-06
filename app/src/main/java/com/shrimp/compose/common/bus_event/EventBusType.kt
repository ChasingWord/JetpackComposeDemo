package com.shrimp.compose.common.bus_event

/**
 * Created by chasing on 2022/4/22.
 */
data class EventScrollToTop(val type: Int) {
    companion object{
        const val TYPE_HOME_MAIN = 0
        const val TYPE_HOME_COMMUNITY = 1
    }
}

data class EventTopicPraise(val topicId: Int, val isPraise: Boolean)