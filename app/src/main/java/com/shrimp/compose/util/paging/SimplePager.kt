package com.shrimp.compose.util.paging

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.shrimp.base.utils.L
import com.shrimp.base.utils.showToast
import com.shrimp.network.entity.base.HttpResult
import com.shrimp.network.entity.base.ResponseResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

fun <T : Any> ViewModel.simplePager(
    config: AppPagingConfig = AppPagingConfig(),
    callAction: suspend (page: Int) -> ResponseResult<List<T>>,
): Flow<PagingData<T>> = pager(config, 0) {
    val page = it.key ?: 0
    val httpResult = try {
        val responseResult = callAction.invoke(page)
        if (responseResult.isSuccess())
            HttpResult.Success(responseResult)
        else
            HttpResult.Error(Exception(responseResult.getErrorMsg()))
    } catch (e: Exception) {
        L.e("request exception: $e")
        if (e is CancellationException)
            HttpResult.Error(Exception("cancel"))
        else
            HttpResult.Error(Exception(ResponseResult.getErrorMsg(e.toString())))
    }
    when (httpResult) {
        is HttpResult.Success -> {
            val data = httpResult.result.data
            val hasNotNext = data.isNullOrEmpty() or (page >= 3)
            PagingSource.LoadResult.Page(
                data = data!!,
                prevKey = if (page - 1 > 0) page - 1 else null,
                nextKey = if (hasNotNext) null else page + 1
            )
        }
        is HttpResult.Error -> {
            val msg = httpResult.exception.message
            if (!TextUtils.isEmpty(msg) && "cancel" != msg)
                showToast(msg)
            PagingSource.LoadResult.Error(httpResult.exception)
        }
    }
}

fun <K : Any, V : Any> ViewModel.pager(
    config: AppPagingConfig = AppPagingConfig(),
    initialKey: K? = null,
    loadData: suspend (PagingSource.LoadParams<K>) -> PagingSource.LoadResult<K, V>,
): Flow<PagingData<V>> = Pager(
    config = PagingConfig(
        config.pageSize,
        initialLoadSize = config.initialLoadSize,
        prefetchDistance = config.prefetchDistance,
        maxSize = config.maxSize,
        enablePlaceholders = config.enablePlaceholders
    ),
    initialKey = initialKey
) {
    object : PagingSource<K, V>() {
        override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
            return loadData.invoke(params)
        }

        override fun getRefreshKey(state: PagingState<K, V>): K? {
            return initialKey
        }

    }
}.flow.cachedIn(viewModelScope)
