package com.shrimp.compose.screen.main.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrimp.compose.bean.TopicData
import com.shrimp.compose.common.bus_event.EventTopicPraise
import com.shrimp.compose.common.bus_event.EventScrollToTop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

/**
 * Created by chasing on 2022/4/22.
 */
@HiltViewModel
class VMHomeCommunity @Inject constructor() : ViewModel() {
    var position by mutableStateOf(0)

    var focusScrollState: LazyListState? = null
    var recommendScrollState: LazyListState? = null
    var answerScrollState: LazyListState? = null
    var workScrollState: LazyListState? = null
    var studentScrollState: LazyListState? = null

    var focusData = mutableStateListOf<TopicData>()

    init {
        EventBus.getDefault().register(this)
        val data = ArrayList<TopicData>()
        for (i in 0..9)
            data.add(TopicData(i, i % 3))
        focusData.addAll(data)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun scrollToTop(event: EventScrollToTop) {
        if (event.type == EventScrollToTop.TYPE_HOME_COMMUNITY) {
            viewModelScope.launch {
                when (position) {
                    0 -> {
                        focusData[1] = focusData[1].copy(isPraise = true)
                        focusScrollState?.scrollToItem(0)
                    }
                    1 -> recommendScrollState?.scrollToItem(0)
                    2 -> answerScrollState?.scrollToItem(0)
                    3 -> workScrollState?.scrollToItem(0)
                    4 -> studentScrollState?.scrollToItem(0)
                }
            }
        }
    }

    @Subscribe
    fun praise(eventTopicPraise: EventTopicPraise){
        for ((index, topicData) in focusData.withIndex()) {
            if (topicData.id == eventTopicPraise.topicId){
                focusData[index] = focusData[index].copy(isPraise = eventTopicPraise.isPraise)
                break;
            }
        }
    }
}

class TestA(val ma:Int){
    fun te(){
        ma + 1
    }
}