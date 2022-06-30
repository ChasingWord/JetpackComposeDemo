package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.base.widgets.noRippleClickable
import com.shrimp.compose.R
import com.shrimp.compose.bean.TopicData
import com.shrimp.compose.screen.main.vm.VMHomeCommunity
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.widgets.TopicItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by chasing on 2022/4/12.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeCommunity(
    navCtrl: NavHostController,
    vmHomeCommunity: VMHomeCommunity = hiltViewModel(),
) {
    SystemStatusBarTransparent(true)
    Column(modifier = Modifier
        .background(color = AppTheme.colors.primary)
        .fillMaxWidth()
        .fillMaxHeight()) {
        val typeList = listOf("关注", "推荐", "问答", "作业", "学生")
        val pagerState = rememberPagerState(initialPage = 0)
        Row(modifier = Modifier
            .statusBarsPadding()
            .padding(AppTheme.dimen.safeSpace, 0.dp)
            .height(AppTheme.dimen.toolbarHeight), verticalAlignment = Alignment.CenterVertically) {
            val modifier = Modifier
                .background(color = AppTheme.colors.background,
                    shape = RoundedCornerShape(15.dp))
                .height(31.dp)
            Row(modifier = modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.search_black_57x54),
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp, 0.dp))
                Text(
                    text = "搜索教程或资源关键词",
                    fontSize = 13.sp,
                    color = AppTheme.colors.textSecondary,
                )
            }
            Box(modifier = Modifier.width(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(11.dp, 0.dp)) {
                Image(painter = painterResource(R.drawable.icon_question_54),
                    contentDescription = null,
                    modifier = Modifier.padding(0.dp, 0.dp, 2.dp, 0.dp))
                Text(text = "玩法", fontSize = 13.sp,
                    color = AppTheme.colors.textPrimary)
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
                HomeCommunitySingleTab(modifier = modifier,
                    textColor = if (index == vmHomeCommunity.position) AppTheme.colors.textPrimary else AppTheme.colors.textSecondary,
                    title = title,
                    index == vmHomeCommunity.position) {
                    vmHomeCommunity.position = index
                    CoroutineScope(Dispatchers.Main).launch {
                        pagerState.scrollToPage(vmHomeCommunity.position,
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
                0 -> HomeCommunityFocus(navCtrl, vmHomeCommunity)
                1 -> HomeCommunityRecommend(navCtrl, vmHomeCommunity)
                2 -> HomeCommunityAnswer(navCtrl, vmHomeCommunity)
                3 -> HomeCommunityWork(navCtrl, vmHomeCommunity)
                4 -> HomeCommunityStudent(navCtrl, vmHomeCommunity)
            }
        }
        vmHomeCommunity.position = pagerState.currentPage
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
    Box(modifier = modifier
        .noRippleClickable(onClick)
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
                .background(AppTheme.colors.confirm))
    }
}

@Composable
fun HomeCommunityFocus(navCtrl: NavHostController, vmHomeCommunity: VMHomeCommunity) {
    val list = vmHomeCommunity.focusData

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        if (vmHomeCommunity.focusScrollState == null)
            vmHomeCommunity.focusScrollState = rememberLazyListState()
        LazyColumn(state = vmHomeCommunity.focusScrollState!!) {
            items(list.size) {
                TopicItem(navCtrl, list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
                withContext(Dispatchers.Main) {
                    vmHomeCommunity.focusData.add(0, TopicData(vmHomeCommunity.focusData.size, 1))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityRecommend(navCtrl: NavHostController, vmHomeCommunity: VMHomeCommunity) {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i, i % 3))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        if (vmHomeCommunity.recommendScrollState == null)
            vmHomeCommunity.recommendScrollState = rememberLazyListState()
        LazyColumn(state = vmHomeCommunity.recommendScrollState!!) {
            items(list.size) {
                TopicItem(navCtrl, list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(list.size, 0))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityAnswer(navCtrl: NavHostController, vmHomeCommunity: VMHomeCommunity) {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i, i % 3))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        if (vmHomeCommunity.answerScrollState == null)
            vmHomeCommunity.answerScrollState = rememberLazyListState()
        LazyColumn(state = vmHomeCommunity.answerScrollState!!) {
            items(list.size) {
                TopicItem(navCtrl, list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(list.size, 0))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityWork(navCtrl: NavHostController, vmHomeCommunity: VMHomeCommunity) {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i, i % 3))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        if (vmHomeCommunity.workScrollState == null)
            vmHomeCommunity.workScrollState = rememberLazyListState()
        LazyColumn(state = vmHomeCommunity.workScrollState!!) {
            items(list.size) {
                TopicItem(navCtrl, list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(list.size, 0))
                    isRefreshing = false
                }
            }
        }
    }
}

@Composable
fun HomeCommunityStudent(navCtrl: NavHostController, vmHomeCommunity: VMHomeCommunity) {
    val list = remember { mutableListOf<TopicData>() }
    for (i in 0..100)
        list.add(TopicData(i, i % 3))

    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
        if (vmHomeCommunity.studentScrollState == null)
            vmHomeCommunity.studentScrollState = rememberLazyListState()
        LazyColumn(state = vmHomeCommunity.studentScrollState!!) {
            items(list.size) {
                TopicItem(navCtrl, list[it])
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect("Refresh") {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
                withContext(Dispatchers.Main) {
                    list.add(0, TopicData(list.size, 0))
                    isRefreshing = false
                }
            }
        }
    }
}