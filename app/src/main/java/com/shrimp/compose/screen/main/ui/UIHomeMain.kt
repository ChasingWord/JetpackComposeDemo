package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.compose.R
import com.shrimp.compose.bean.*
import com.shrimp.compose.screen.main.vm.VMHomeMain
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_31353a
import com.shrimp.compose.ui.theme.color_f1f3f5
import com.shrimp.compose.ui.theme.color_ffffff
import com.shrimp.compose.ui.widgets.Banner
import com.shrimp.compose.ui.widgets.SNACK_ERROR
import com.shrimp.compose.ui.widgets.TopicItem
import com.shrimp.compose.ui.widgets.popupSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.ceil

/**
 * Created by chasing on 2022/3/22.
 */
@Preview
@Composable
fun HomeMainPreview() {
    val navCtrl = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    HomeMain(navCtrl, scaffoldState)
}

@Composable
fun HomeMain(
    navCtrl: NavHostController, scaffoldState: ScaffoldState,
    vmHomeMain: VMHomeMain = hiltViewModel(),
) {
    val dp100ToPx = with(LocalDensity.current) { 100.dp.toPx() }

    Box(modifier = Modifier.background(color = AppTheme.colors.primary)) {
        var alpha by remember { mutableStateOf(0f) }
        var isBlackType by remember { mutableStateOf(false) }
        val totalScrollTemp = vmHomeMain.totalScroll.observeAsState()
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    vmHomeMain.totalScroll.value = (totalScrollTemp.value ?: 0f) + available.y
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
            vmHomeMain.scrollState = rememberLazyListState()
            LazyColumn(modifier = Modifier.nestedScroll(nestedScrollConnection),
                state = vmHomeMain.scrollState!!) {
                item {
                    HomeMainAdvBanner()
                }
                item {
                    HomeMainChannel(scaffoldState)
                }
                item {
                    HomeMainHottestCourse()
                }
                item {
                    HomeMainResource()
                }
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
                    vmHomeMain.totalScroll.value = 0f
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
            alpha = alpha), isBlackType, vmHomeMain)
        SystemStatusBarTransparent(isBlackType)
    }
}

@Composable
fun HomeMainSearchBar(modifier: Modifier, isBlackType: Boolean, vmHomeMain: VMHomeMain) {
    Box(modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(AppTheme.dimen.safeSpace, 7.dp, AppTheme.dimen.safeSpace, 7.dp),
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
                modifier = subModifier
                    .weight(1f)
                    .clickable(indication = null, interactionSource = remember {
                        MutableInteractionSource()
                    }, onClick = {
                        vmHomeMain.changeHint()
                    })) {
                Image(painter = painterResource(
                    id = if (isBlackType)
                        R.mipmap.search_black_57x54
                    else
                        R.mipmap.search_57x54),
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp, 0.dp))
                Text(text = vmHomeMain.searchHint, fontSize = 13.sp,
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

    Banner(bannerInfoList = bannerInfoList, bannerHeight = 234.dp)
}

@Composable
fun HomeMainChannel(scaffoldState: ScaffoldState) {
    Row(modifier = Modifier.padding(0.dp, 10.dp)) {
        val titleList = listOf("学习", "教程", "资源", "发现", "王座杯")
        val imgList = listOf(R.mipmap.home_channel_learn, R.mipmap.home_channel_course,
            R.mipmap.home_channel_resource, R.mipmap.home_channel_find,
            R.mipmap.home_channel_throne_cup)
        val modifier = Modifier.weight(1f)
        for (index in titleList.indices) {
            HomeMainChannelSingle(modifier = modifier, imgList[index], titleList[index]) {
                popupSnackBar(CoroutineScope(Dispatchers.Main),
                    scaffoldState,
                    label = SNACK_ERROR,
                    titleList[index])
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
        Text(text = title, color = AppTheme.colors.textPrimary,
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
            color = AppTheme.colors.textPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(AppTheme.dimen.safeSpace, 0.dp, 0.dp, 14.dp))
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
                        .padding(AppTheme.dimen.safeSpace, 0.dp, 5.dp, 10.dp)
                        .border(BorderStroke(1.dp, AppTheme.colors.divider),
                            RoundedCornerShape(4.dp)),
                        courseList[index * 2])
                    HomeMainHottestCourseSingle(modifier
                        .padding(5.dp, 0.dp, AppTheme.dimen.safeSpace, 10.dp)
                        .border(BorderStroke(1.dp, AppTheme.colors.divider),
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
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 0.dp),
            maxLines = 1)
        Text(text = courseInfo.subTitle,
            fontSize = 10.sp,
            color = AppTheme.colors.textSecondary,
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
            color_f1f3f5, color_ffffff
        )))) {
        Row(modifier = Modifier.padding(AppTheme.dimen.safeSpace,
            20.dp,
            AppTheme.dimen.safeSpace,
            0.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "排行榜", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.weight(1f))

            Text(text = "更多", fontSize = 12.sp, color = AppTheme.colors.textSecondary)
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
                        AppTheme.colors.confirm
                    else
                        AppTheme.colors.textSecondary),
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
fun HomeMainResourceTab(
    modifier: Modifier,
    textColor: Color,
    resourceInfoGroup: ResourceInfoGroup,
) {
    Text(text = resourceInfoGroup.groupName,
        fontSize = 16.sp,
        color = textColor,
        modifier = modifier.padding(AppTheme.dimen.safeSpace, 10.dp))
}

@Composable
fun HomeMainResourceSingle(modifier: Modifier, resourceInfo: ResourceInfo) {
    Row(modifier = modifier.padding(AppTheme.dimen.safeSpace, 0.dp, 10.dp, 10.dp),
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
                color = color_31353a)
            Text(text = "${resourceInfo.saleCount}销量", fontSize = 12.sp,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp))
            Text(text = "${resourceInfo.fileSize}MB", fontSize = 12.sp,
                color = AppTheme.colors.textSecondary,
                modifier = Modifier.padding(0.dp, 1.dp, 0.dp, 0.dp))
        }
    }
}