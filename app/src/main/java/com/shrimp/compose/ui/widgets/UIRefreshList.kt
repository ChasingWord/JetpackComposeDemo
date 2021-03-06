package com.shrimp.compose.ui.widgets

import android.R
import androidx.compose.animation.core.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.shrimp.compose.ui.theme.AppTheme

@Composable
fun <T : Any> RefreshList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit) = {},
    listState: LazyListState = rememberLazyListState(),
    itemContent: LazyListScope.() -> Unit,
) {
    val rememberSwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    //错误页
    val err = lazyPagingItems.loadState.refresh is LoadState.Error
    if (err) {
        ErrorContent { lazyPagingItems.retry() }
        return
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState,
        onRefresh = {
            onRefresh.invoke()
            lazyPagingItems.refresh()
        },
        refreshTriggerDistance = 50.dp, //刷新触发距离
//        indicator = { s, _ ->
//            CustomSwipeRefreshIndicator(s)
//        },
    ) {
        //刷新状态
        rememberSwipeRefreshState.isRefreshing =
            ((lazyPagingItems.loadState.refresh is LoadState.Loading) || isRefreshing)
        //列表
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            //条目布局
            itemContent()
            //加载更多状态：加载中和加载错误,没有更多
            if (!rememberSwipeRefreshState.isRefreshing) {
                item {
                    lazyPagingItems.apply {
                        when (loadState.append) {
                            is LoadState.Loading -> LoadingItem()
                            is LoadState.Error -> ErrorItem { retry() }
                            is LoadState.NotLoading -> {
                                if (loadState.append.endOfPaginationReached) {
                                    NoMoreItem()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorContent(retry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(id = R.drawable.stat_notify_error),
                contentDescription = null,
                colorFilter = ColorFilter.tint(AppTheme.colors.error),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "请求出错啦",
                color = AppTheme.colors.textPrimary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp)
            )
            Button(
                onClick = { retry() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp),
                colors = buttonColors(backgroundColor = AppTheme.colors.secondary)
            ) {
                Text(text = "重试")
            }
        }
    }
}

@Composable
fun ErrorItem(retry: () -> Unit) {
    Button(
        onClick = { retry() },
        modifier = Modifier.padding(10.dp),
        colors = buttonColors(backgroundColor = AppTheme.colors.secondary)
    ) {
        Text(text = "重试")
    }
}

@Composable
fun NoMoreItem() {
    Text(
        text = "没有更多了",
        color = AppTheme.colors.textSecondary,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = AppTheme.colors.secondary,
            modifier = Modifier
                .padding(10.dp)
                .height(50.dp)
        )
    }
}

@Composable
fun CustomSwipeRefreshIndicator(
    state: SwipeRefreshState,
    refreshingOffset: Dp = 16.dp,
) {
    val size = 20.dp
    val indicatorHeight = with(LocalDensity.current) { size.roundToPx() }
    val refreshingOffsetPx = with(LocalDensity.current) { refreshingOffset.toPx() }
    var offset by remember { mutableStateOf(0f) }
    if (state.isSwipeInProgress) {
        // If the user is currently swiping, we use the 'slingshot' offset directly
        offset = state.indicatorOffset
    } else {
        // If there's no swipe currently in progress, animate to the correct resting position
        LaunchedEffect(state.isRefreshing) {
            animate(
                initialValue = offset,
                targetValue = when {
                    state.isRefreshing -> indicatorHeight + refreshingOffsetPx
                    else -> 0f
                }
            ) { value, _ ->
                offset = value
            }
        }
    }

    Surface(
        modifier = Modifier
            .height(size)
            .graphicsLayer {
                // Translate the indicator according to the slingshot
                translationY = offset - indicatorHeight
            },
    ) {
        // This shows either an Image with CircularProgressPainter or a CircularProgressIndicator,
        // depending on refresh state
        Text(text = "更新...", fontSize = 14.sp, color = AppTheme.colors.textPrimary)
    }
}
