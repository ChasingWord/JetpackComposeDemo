package com.shrimp.compose.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.compose.R
import com.shrimp.compose.bean.TopicData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by chasing on 2022/4/12.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeCommunity() {
    SystemStatusBarTransparent(true)
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val typeList = listOf("关注", "推荐", "问答", "作业", "学生")
        var position by remember { mutableStateOf(0) }
        val pagerState = rememberPagerState(initialPage = 0)
        Row(modifier = Modifier
            .statusBarsPadding()
            .padding(13.dp, 0.dp)
            .height(42.dp), verticalAlignment = Alignment.CenterVertically) {
            val modifier = Modifier
                .background(color = colorResource(R.color.color_f6f8fa),
                    shape = RoundedCornerShape(15.dp))
                .height(31.dp)
            Row(modifier = modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.mipmap.search_black_57x54),
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp, 0.dp))
                Text(
                    text = "搜索教程或资源关键词",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.color_a6a9ad),
                )
            }
            Box(modifier = Modifier.width(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(11.dp, 0.dp)) {
                Image(painter = painterResource(R.mipmap.icon_question_54),
                    contentDescription = null,
                    modifier = Modifier.padding(0.dp, 0.dp, 2.dp, 0.dp))
                Text(text = "玩法", fontSize = 13.sp,
                    color = colorResource(R.color.color_282a2e))
            }
        }
        Row(modifier = Modifier
            .height(41.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            val modifier = Modifier
                .padding(15.dp, 0.dp)
                .fillMaxHeight()
            for ((index, title) in typeList.withIndex()) {
                val color =
                    colorResource(id = if (index == position) R.color.color_282a2e else R.color.color_a6a9ad)
                HomeCommunitySingleTab(modifier = modifier,
                    textColor = color,
                    title = title,
                    index == position) {
                    position = index
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(position,
                            0f)
                    }
                }
            }
        }
        HorizontalPager(count = typeList.size, state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()) { index ->
            when (index) {
                0 -> HomeCommunityFocus()
                1 -> HomeCommunityRecommend()
                2 -> HomeCommunityAnswer()
                3 -> HomeCommunityWork()
                4 -> HomeCommunityStudent()
            }
        }
        position = pagerState.currentPage
    }
}

@Composable
fun HomeCommunitySingleTab(
    modifier: Modifier,
    textColor: Color,
    title: String,
    isSelect: Boolean,
    onClick: () -> Unit,
) {
    // 去除点击效果
    Box(modifier = modifier
        .clickable(onClick = { onClick.invoke() }, indication = null, interactionSource = remember {
            MutableInteractionSource()
        })
        .width(IntrinsicSize.Max),
        contentAlignment = Alignment.Center) {
        Text(text = title,
            color = textColor,
            fontSize = 17.sp)
        if (isSelect)
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(3.dp)
                .background(colorResource(id = R.color.color_ff609d)))
    }
}

var HomeCommunityFocusScrollState: LazyListState? = null
var HomeCommunityRecommendScrollState: LazyListState? = null
var HomeCommunityAnswerScrollState: LazyListState? = null
var HomeCommunityWorkScrollState: LazyListState? = null
var HomeCommunityStudentScrollState: LazyListState? = null

@Composable
fun HomeCommunityFocus() {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i % 3, false))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        HomeCommunityFocusScrollState = rememberLazyListState()
        LazyColumn(state = HomeCommunityFocusScrollState!!) {
            items(list.size) {
                TopicItem(topicData = list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(0, false))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityRecommend() {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i % 3, false))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        HomeCommunityRecommendScrollState = rememberLazyListState()
        LazyColumn(state = HomeCommunityRecommendScrollState!!) {
            items(list.size) {
                TopicItem(topicData = list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(0, false))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityAnswer() {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i % 3, false))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        HomeCommunityAnswerScrollState = rememberLazyListState()
        LazyColumn(state = HomeCommunityAnswerScrollState!!) {
            items(list.size) {
                TopicItem(topicData = list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(0, false))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityWork() {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i % 3, false))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        HomeCommunityWorkScrollState = rememberLazyListState()
        LazyColumn(state = HomeCommunityWorkScrollState!!) {
            items(list.size) {
                TopicItem(topicData = list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(0, false))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityStudent() {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i % 3, false))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        HomeCommunityStudentScrollState = rememberLazyListState()
        LazyColumn(state = HomeCommunityStudentScrollState!!) {
            items(list.size) {
                TopicItem(topicData = list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(0, false))
                    isRefreshing = false
                }
            }
        }
    }
}