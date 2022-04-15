package com.shrimp.compose.screen.main.ui

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.compose.R
import com.shrimp.compose.bean.*
import com.shrimp.compose.ui.view.TopicItem
import com.shrimp.compose.util.floorMod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.ceil

/**
 * Created by chasing on 2022/3/22.
 */
@Preview
@Composable
fun HomeMainPreview() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(colorResource(id = R.color.color_f5f5f5))) {
        HomeMain()

    }
}

var HomeMainTotalScroll = MutableLiveData(0f)
var HomeMainScrollState: LazyListState? = null

@Composable
fun HomeMain() {
    val dp100ToPx = with(LocalDensity.current) { 100.dp.toPx() }

    Box(modifier = Modifier.background(color = colorResource(id = R.color.white))) {
        var alpha by remember { mutableStateOf(0f) }
        var isBlackType by remember { mutableStateOf(false) }
        val totalScrollTemp = HomeMainTotalScroll.observeAsState()
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    HomeMainTotalScroll.value = (totalScrollTemp.value ?: 0f) + available.y
                    return Offset.Zero
                }
            }
        }
        val list = remember { mutableListOf<TopicData>() }
        for (i in 0..100)
            list.add(TopicData(i % 3, false))

        var isRefreshing by remember { mutableStateOf(false) }
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
        SwipeRefresh(state = swipeRefreshState, onRefresh = { isRefreshing = true }) {
            HomeMainScrollState = rememberLazyListState()
            LazyColumn(modifier = Modifier.nestedScroll(nestedScrollConnection),
                state = HomeMainScrollState!!) {
                item {
                    HomeMainAdvBanner()
                }
                item {
                    HomeMainChannel()
                }
                item {
                    HomeMainHottestCourse()
                }
                item {
                    HomeMainResource()
                }
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

        // 根据滚动距离切换toolbar样式
        totalScrollTemp.value?.let {
            if (it != 0f) {
                val alphaTemp = if (it < 0f) {
                    val tempAlpha = abs(it / dp100ToPx)
                    tempAlpha.coerceAtMost(1f)
                } else {
                    HomeMainTotalScroll.value = 0f
                    0f
                }
                if (alpha != alphaTemp)
                    alpha = alphaTemp
                val isBlack = alpha > 0.6
                if (isBlackType != isBlack)
                    isBlackType = isBlack
            } else if (alpha > 0f) {
                isBlackType = false
                alpha = 0f
            }
        }

        HomeMainSearchBar(Modifier.background(
            brush = Brush.verticalGradient(colors = listOf(Color.White, Color.White)),
            alpha = alpha), isBlackType)
        SystemStatusBarTransparent(isBlackType)
    }
}

@Composable
fun HomeMainSearchBar(modifier: Modifier, isBlackType: Boolean) {
    Box(modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(13.dp, 7.dp, 13.dp, 7.dp),
            verticalAlignment = Alignment.CenterVertically) {
            val subModifier = Modifier
                .background(color = colorResource(
                    id = if (isBlackType)
                        R.color.color_f6f8fa
                    else
                        R.color.p10_transition),
                    shape = RoundedCornerShape(15.dp))
                .fillMaxHeight()
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = subModifier.weight(1f)) {
                Image(painter = painterResource(
                    id = if (isBlackType)
                        R.mipmap.search_black_57x54
                    else
                        R.mipmap.search_57x54),
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp, 0.dp))
                Text(text = "搜索教程或资源关键词", fontSize = 13.sp,
                    color = colorResource(
                        id = if (isBlackType)
                            R.color.color_a6a9ad
                        else
                            R.color.white))
            }
            Box(modifier = Modifier.width(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = subModifier.padding(11.dp, 0.dp)) {
                Image(painter = painterResource(
                    id = if (isBlackType)
                        R.mipmap.home_main_label_black
                    else
                        R.mipmap.home_main_label),
                    contentDescription = null,
                    modifier = Modifier.padding(0.dp, 0.dp, 2.dp, 0.dp))
                Text(text = "分类", fontSize = 13.sp,
                    color = colorResource(
                        id = if (isBlackType)
                            R.color.color_282a2e
                        else
                            R.color.white))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeMainAdvBanner() {
    val bannerInfoList = ArrayList<BannerInfo>()
    bannerInfoList.add(BannerInfo(R.mipmap.banner_1))
    bannerInfoList.add(BannerInfo(R.mipmap.banner_2))
    bannerInfoList.add(BannerInfo(R.mipmap.banner_3))
    bannerInfoList.add(BannerInfo(R.mipmap.banner_4))

    Box {
        // We start the pager in the middle of the raw number of pages
        val startIndex = Int.MAX_VALUE / 2
        val pagerState = rememberPagerState(initialPage = startIndex)
        HorizontalPager(
            // Set the raw page count to a really large number
            count = Int.MAX_VALUE,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
//        contentPadding = PaddingValues(horizontal = 0.dp),
            // Add some horizontal spacing between items
//            itemSpacing = 0.dp,
            modifier = Modifier
                .height(234.dp)
                .fillMaxWidth()
        ) { index ->
            // We calculate the page from the given index
            val page = (index - startIndex).floorMod(bannerInfoList.size)
            HomeMainAdvBannerSingle(
                bannerInfo = bannerInfoList[page],
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }

        var time by remember { mutableStateOf(System.currentTimeMillis()) }
        //自动滚动
        if (pagerState.pageCount > 1) {
            LaunchedEffect(time) {
                withContext(Dispatchers.IO) {
                    delay(3000)
                    withContext(Dispatchers.Main) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1, 0f)
                        time = System.currentTimeMillis()
                    }
                }
            }
        }

        Row(modifier = Modifier
            .padding(10.dp)
            .align(Alignment.BottomCenter)) {
            val page = (pagerState.currentPage - startIndex).floorMod(bannerInfoList.size)
            for (i in bannerInfoList.indices) {
                Box(modifier = Modifier
                    .width(10.dp)
                    .height(6.dp)
                    .padding(0.dp, 0.dp, 4.dp, 0.dp)
                    .background(color = colorResource(
                        id = if (page == i)
                            R.color.black
                        else
                            R.color.color_f1f3f5),
                        shape = CircleShape))
            }
        }
    }
}

@Composable
fun HomeMainAdvBannerSingle(bannerInfo: BannerInfo, modifier: Modifier) {
    Image(painter = painterResource(id = bannerInfo.resId), contentDescription = null,
        modifier = modifier, contentScale = ContentScale.FillBounds)
}

@Composable
fun HomeMainChannel() {
    Row(modifier = Modifier.padding(0.dp, 10.dp)) {
        val titleList = listOf("学习", "教程", "资源", "发现", "王座杯")
        val imgList = listOf(R.mipmap.home_channel_learn, R.mipmap.home_channel_course,
            R.mipmap.home_channel_resource, R.mipmap.home_channel_find,
            R.mipmap.home_channel_throne_cup)
        val context = LocalContext.current
        val modifier = Modifier.weight(1f)
        for (index in titleList.indices) {
            HomeMainChannelSingle(modifier = modifier, imgList[index], titleList[index]) {
                Toast.makeText(context, titleList[index], Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun HomeMainChannelSingle(
    modifier: Modifier, resId: Int, title: String,
    onClick: () -> Unit,
) {
    Column(modifier = modifier.clickable { onClick.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = resId), contentDescription = null)
        Text(text = title, color = colorResource(id = R.color.color_282a2e),
            fontSize = 15.sp)
    }
}

@Composable
fun HomeMainHottestCourse() {
    Column {
        /*val annotatedText = buildAnnotatedString {
            append("查看详情:")
            // We attach this *URL* annotation to the following content
            // until `pop()` is called
            pushStringAnnotation(tag = "URL", annotation = "https://developer.android.com")
            withStyle(style = SpanStyle(color = Color.Blue,
                fontWeight = FontWeight.Bold)) {
                append("here")
            }
            pop()
        }
        ClickableText(text = annotatedText) {
            annotatedText
                .getStringAnnotations(tag = "URL", start = it,
                    end = it)
                .firstOrNull()
                ?.let { annotation ->
                    Log.d("Clicked URL", annotation.item)
                }
        }*/

        Text(text = "今日最热", fontSize = 19.sp,
            color = colorResource(id = R.color.color_282a2e),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(13.dp, 0.dp, 0.dp, 14.dp))
        val courseList = listOf(
            CourseInfo(0,
                "原画入门基础",
                "共9课时·1111人学习",
                "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF"),
            CourseInfo(1,
                "原画初级",
                "共9课时·1111人学习",
                ""),
            CourseInfo(2,
                "原画进阶",
                "共9课时·1111人学习",
                "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF"),
            CourseInfo(3,
                "原画高级",
                "共9课时·1111人学习",
                "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF"))
        val modifier = Modifier
            .weight(1f)
        for (index in 0 until ceil(courseList.size / 2f).toInt()) {
            Box {
                Row {
                    HomeMainHottestCourseSingle(modifier
                        .padding(13.dp, 0.dp, 5.dp, 10.dp)
                        .border(BorderStroke(1.dp, colorResource(id = R.color.color_f1f3f5)),
                            RoundedCornerShape(4.dp)),
                        courseList[index * 2])
                    HomeMainHottestCourseSingle(modifier
                        .padding(5.dp, 0.dp, 13.dp, 10.dp)
                        .border(BorderStroke(1.dp, colorResource(id = R.color.color_f1f3f5)),
                            RoundedCornerShape(4.dp)),
                        courseList[index * 2 + 1])
                }
                if (index == 0) {
                    val imageBitmap = ImageBitmap.imageResource(id = R.mipmap.icon_work_pass)
                    Canvas(modifier = Modifier.align(Alignment.TopEnd)) {
                        drawImage(image = imageBitmap,
                            topLeft = Offset(-imageBitmap.width - 20.dp.toPx(),
                                -imageBitmap.height / 2f))
                    }
                }
            }
        }
    }
}

@Composable
fun HomeMainHottestCourseSingle(modifier: Modifier, courseInfo: CourseInfo) {
    Column(modifier = modifier) {
        AsyncImage(modifier = Modifier
            .clip(RoundedCornerShape(4.dp, 4.dp, 0.dp, 0.dp))
            .fillMaxWidth()
            .aspectRatio(161 / 102f),
            model = courseInfo.imgUrl, contentDescription = null,
            error = painterResource(id = R.mipmap.default_pic),
            contentScale = ContentScale.Crop)
        Text(text = courseInfo.title,
            fontSize = 15.sp,
            color = colorResource(id = R.color.color_282a2e),
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 0.dp),
            maxLines = 1)
        Text(text = courseInfo.subTitle,
            fontSize = 10.sp,
            color = colorResource(id = R.color.color_a6a9ad),
            modifier = Modifier.padding(10.dp, 3.dp, 10.dp, 10.dp),
            maxLines = 1)
    }
}

@Composable
fun HomeMainResource() {
    val context = LocalContext.current
    Column(modifier = Modifier
        .padding(0.dp, 10.dp)
        .background(brush = Brush.verticalGradient(colors = listOf(
            Color(ContextCompat.getColor(context, R.color.color_f1f3f5)),
            Color(ContextCompat.getColor(context, R.color.white))
        )))) {
        Row(modifier = Modifier.padding(13.dp, 20.dp, 13.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "排行榜", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.color_2c2c2c),
                modifier = Modifier.weight(1f))

            Text(text = "更多", fontSize = 12.sp, color = colorResource(id = R.color.color_777e89))
            Image(painter = painterResource(id = R.mipmap.arrow_right_36),
                contentDescription = null)
        }

        val resourceGroupList = mutableListOf<ResourceInfoGroup>()
        for (index in 0..5) {
            val resourceList = mutableListOf<ResourceInfo>()
            for (position in 0..10) {
                resourceList.add(ResourceInfo("${index}类-素材资源",
                    position,
                    position,
                    "https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF"))
            }
            resourceGroupList.add(ResourceInfoGroup("${index}类-xxxx", resourceList))
        }

        var position by remember { mutableStateOf(0) }
        LazyRow {
            items(count = resourceGroupList.size) {
                HomeMainResourceTab(
                    Modifier.clickable { position = it },
                    (if (position == it)
                        R.color.color_ff609d
                    else
                        R.color.color_a6a9ad),
                    resourceGroupList[it])
            }
        }
        val modifier = Modifier
            .width(264.dp)
            .height(74.dp)
        LazyRow {
            items(count = ceil(resourceGroupList[position].dataList.size / 3f).toInt()) {
                Column {
                    HomeMainResourceSingle(modifier = modifier,
                        resourceInfo = resourceGroupList[position].dataList[it * 3])
                    if (it * 3 + 1 < resourceGroupList[position].dataList.size)
                        HomeMainResourceSingle(modifier = modifier,
                            resourceInfo = resourceGroupList[position].dataList[it * 3 + 1])
                    if (it * 3 + 2 < resourceGroupList[position].dataList.size)
                        HomeMainResourceSingle(modifier = modifier,
                            resourceInfo = resourceGroupList[position].dataList[it * 3 + 2])
                }
            }
        }
    }
}

@Composable
fun HomeMainResourceTab(modifier: Modifier, textColor: Int, resourceInfoGroup: ResourceInfoGroup) {
    Text(text = resourceInfoGroup.groupName,
        fontSize = 16.sp,
        color = colorResource(id = textColor),
        modifier = modifier.padding(13.dp, 10.dp))
}

@Composable
fun HomeMainResourceSingle(modifier: Modifier, resourceInfo: ResourceInfo) {
    Row(modifier = modifier.padding(13.dp, 0.dp, 10.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp)),
            model = resourceInfo.imgUrl, contentDescription = null,
            error = painterResource(id = R.mipmap.default_pic))
        Column(modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)) {
            Text(text = resourceInfo.title,
                fontSize = 14.sp,
                color = colorResource(id = R.color.color_31353a))
            Text(text = "${resourceInfo.saleCount}销量", fontSize = 12.sp,
                color = colorResource(id = R.color.color_a6a9ad),
                modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp))
            Text(text = "${resourceInfo.fileSize}MB", fontSize = 12.sp,
                color = colorResource(id = R.color.color_a6a9ad),
                modifier = Modifier.padding(0.dp, 1.dp, 0.dp, 0.dp))
        }
    }
}