package com.shrimp.compose.screen.main.vm

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shrimp.compose.engine.ViewAction
import com.shrimp.compose.util.paging.simplePager
import com.shrimp.network.RequestManager
import com.shrimp.network.entity.res.Tags
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by chasing on 2022/4/20.
 */
@HiltViewModel
class VMLabel @Inject constructor() : ViewModel() {
    private val pager by lazy {
        simplePager {
            delay(2000)
            RequestManager.getCourseTagsMenuByUserId("744677", 311)
        }.cachedIn(viewModelScope)
    }

    var viewStates by mutableStateOf(LabelViewState(pagingData = pager))
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

    fun cancelRequest(){
        viewModelScope.cancel()
        viewStates = LabelViewState(pagingData = pager)
    }
}

data class LabelViewState(
    val pagingData: Flow<PagingData<Tags>>,
    val isRefreshing: Boolean = false,
    val listState: LazyListState = LazyListState(),
)