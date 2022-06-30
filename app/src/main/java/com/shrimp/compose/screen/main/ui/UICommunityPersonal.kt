package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.shrimp.base.utils.GenericTools
import com.shrimp.base.utils.L
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.base.utils.toDp
import com.shrimp.compose.R
import com.shrimp.compose.engine.ViewAction
import com.shrimp.compose.screen.main.vm.VMCommunityPersonal
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_ffffff
import com.shrimp.compose.ui.theme.color_transparent
import com.shrimp.compose.ui.widgets.RefreshList
import com.shrimp.compose.ui.widgets.Toolbar
import com.shrimp.compose.ui.widgets.TopicItem
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by chasing on 2022/4/21.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommunityPersonal(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    userId: Int,
    viewModel: VMCommunityPersonal = hiltViewModel(),
) {
    SystemStatusBarTransparent(isShowDarkIcon = true)
    val viewStates = viewModel.viewStates
    val data = viewStates.pagingData.collectAsLazyPagingItems()
    val isRefreshing = viewStates.isRefreshing
    val listState = viewStates.listState

    var scrolledY = 0f
    var previousOffset = 0

    val state = remember { mutableStateOf(0) }
    val titles = listOf("标签1", "标签2", "这是很长的标签3")

    val screenWidth = GenericTools.getScreenWidth(LocalContext.current)
    val imgHeightPx = screenWidth * 288 / 467
    val imgHeight = imgHeightPx.toDp(LocalContext.current)

    Column {
        Toolbar(navCtrl = navCtrl,
            title = "个人中心",
            backgroundColor = color_ffffff,
            isStatusBarPadding = true)
        RefreshList(lazyPagingItems = data, listState = listState,
            isRefreshing = isRefreshing, onRefresh = {
                viewModel.dispatch(ViewAction.Refresh)
            }) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.banner_1),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .graphicsLayer {
                            scrolledY += listState.firstVisibleItemScrollOffset - previousOffset
                            translationY = scrolledY * 0.5f
                            previousOffset = listState.firstVisibleItemScrollOffset
                        }
                        .height(imgHeight)
                        .fillMaxWidth()
                )
            }

            stickyHeader {
                TabRow(modifier = Modifier
                    .fillMaxWidth()
                    .height(AppTheme.dimen.toolbarHeight),
                    selectedTabIndex = state.value) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = state.value == index,
                            onClick = { state.value = index }
                        )
                    }
                }
            }

            items(data.itemCount) {
                val topic = data[it]
                if (topic != null)
                    TopicItem(navCtrl = navCtrl, topicData = topic)
            }
        }
    }
}