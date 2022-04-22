package com.shrimp.compose.screen.main.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrimp.base.utils.L
import com.shrimp.base.view.BaseViewModel
import com.shrimp.compose.engine.EventScrollToTop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

/**
 * Created by chasing on 2022/4/13.
 */
@HiltViewModel
class VMHomeMain @Inject constructor() : BaseViewModel() {
    var searchHint by mutableStateOf("搜索教程或资源关键词")
    var totalScroll = MutableLiveData(0f)
    var scrollState: LazyListState? = null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                L.e("ON_CREATE");
                EventBus.getDefault().register(this)
            }
            Lifecycle.Event.ON_DESTROY -> {
                EventBus.getDefault().unregister(this)
            }
            else -> {

            }
        }
    }

    fun changeHint() {
        searchHint = "change"
    }

    @Subscribe
    fun scrollToTop(event: EventScrollToTop) {
        if (event.type == EventScrollToTop.TYPE_HOME_MAIN) {
            viewModelScope.launch { scrollState?.scrollToItem(0) }
        }
    }
}