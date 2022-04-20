package com.shrimp.compose.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.shrimp.compose.R
import com.shrimp.compose.bean.BannerInfo
import com.shrimp.compose.util.floorMod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Created by chasing on 2022/4/19.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Banner(
    bannerInfoList: List<BannerInfo>,
    bannerHeight: Dp,
    onClick: (BannerInfo) -> Unit = {},
) {
    if (bannerInfoList.isNotEmpty()) {
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
                    .height(bannerHeight)
                    .fillMaxWidth()
            ) { index ->
                // We calculate the page from the given index
                val page = (index - startIndex).floorMod(bannerInfoList.size)
                BannerSingle(
                    bannerInfo = bannerInfoList[page],
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable { onClick.invoke(bannerInfoList[page]) }
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
}

@Composable
fun BannerSingle(bannerInfo: BannerInfo, modifier: Modifier) {
    Image(painter = painterResource(id = bannerInfo.resId), contentDescription = null,
        modifier = modifier, contentScale = ContentScale.FillBounds)
}