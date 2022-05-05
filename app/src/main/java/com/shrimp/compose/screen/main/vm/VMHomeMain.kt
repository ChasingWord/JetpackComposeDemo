package com.shrimp.compose.screen.main.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrimp.compose.common.bus_event.EventScrollToTop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

/**
 * Created by chasing on 2022/4/13.
 */
@HiltViewModel
class VMHomeMain @Inject constructor() : ViewModel() {
    var searchHint by mutableStateOf("搜索教程或资源关键词")
    var totalScroll = MutableLiveData(0f)
    var scrollState: LazyListState? = null

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun scrollToTop(event: EventScrollToTop) {
        if (event.type == EventScrollToTop.TYPE_HOME_MAIN) {
            totalScroll.value = 0f
            viewModelScope.launch { scrollState?.scrollToItem(0) }
        }
    }
}