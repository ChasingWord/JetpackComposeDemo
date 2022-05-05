package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.compose.R
import com.shrimp.compose.engine.ViewAction
import com.shrimp.compose.screen.main.vm.VMCommunityPersonal
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.widgets.RefreshList
import com.shrimp.compose.ui.widgets.Toolbar
import com.shrimp.compose.ui.widgets.TopicItem

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
    val listState = if (data.itemCount > 0) viewStates.listState else LazyListState()

    var scrolledY = 0f
    var previousOffset = 0

    RefreshList(data, listState = listState, isRefreshing = isRefreshing, onRefresh = {
        viewModel.dispatch(ViewAction.Refresh)
    }) {
        item {
            Image(
                painter = painterResource(id = R.mipmap.banner_1),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .graphicsLayer {
                        scrolledY += listState.firstVisibleItemScrollOffset - previousOffset
                        translationY = scrolledY * 0.5f
                        previousOffset = listState.firstVisibleItemScrollOffset
                    }
                    .height(240.dp)
                    .fillMaxWidth()
            )
        }

        stickyHeader {
            Toolbar(navCtrl = navCtrl,
                title = "个人中心",
                backgroundColor = AppTheme.colors.primary,
                isStatusBarPadding = true)
        }

        items(data.itemCount) {
            val topic = data[it]
            if (topic != null)
                TopicItem(navCtrl = navCtrl, topicData = topic)
        }
    }
}