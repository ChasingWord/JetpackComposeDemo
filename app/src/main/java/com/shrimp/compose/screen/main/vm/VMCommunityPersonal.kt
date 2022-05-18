package com.shrimp.compose.screen.main.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.shrimp.compose.bean.TopicData
import com.shrimp.compose.engine.ViewAction
import com.shrimp.compose.util.paging.simplePager
import com.shrimp.network.entity.base.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by chasing on 2022/4/25.
 */
@HiltViewModel
class VMCommunityPersonal @Inject constructor() : ViewModel() {
    private val pager by lazy {
        simplePager {
            delay(2000)
            val responseResult = ResponseResult<List<TopicData>>()
            responseResult.code = "00"
            val topicDataList = ArrayList<TopicData>()
            for (i in 0..10)
                topicDataList.add(TopicData(i, i % 3))
            responseResult.data = topicDataList
            responseResult
        }
    }

    var viewStates by mutableStateOf(CommunityPersonalViewState(pagingData = pager))
        private set

    init {
        dispatch(ViewAction.FetchData)
    }

    fun dispatch(action: ViewAction) {
        when (action) {
            is ViewAction.FetchData -> fetchData()
            is ViewAction.Refresh -> refresh()
        }
    }

    private fun fetchData() {

    }

    private fun refresh() {
        fetchData()
    }
}

data class CommunityPersonalViewState(
    val pagingData: Flow<PagingData<TopicData>>,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
)