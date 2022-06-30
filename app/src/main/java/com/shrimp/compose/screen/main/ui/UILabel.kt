package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.base.utils.showToast
import com.shrimp.compose.engine.ViewAction
import com.shrimp.compose.screen.main.vm.VMLabel
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.widgets.RefreshList
import com.shrimp.compose.ui.widgets.Toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by chasing on 2022/4/15.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Label(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: VMLabel = hiltViewModel(),
) {
    val viewStates = viewModel.viewStates
    val recommendData = viewStates.pagingData.collectAsLazyPagingItems()
    val isRefreshing = viewStates.isRefreshing
    val listState = viewStates.listState
    SystemStatusBarTransparent(isShowDarkIcon = true)
    Column(modifier = Modifier.statusBarsPadding()) {
        Toolbar(navCtrl = navCtrl, title = "标签列表页", modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        CoroutineScope(Dispatchers.Main).launch {
                            listState.scrollToItem(0)
                            showToast("onDoubleTap")
                        }
                    },
                    onLongPress = { showToast("onLongPress") },
                    onTap = {
                        CoroutineScope(Dispatchers.Main).launch {
                            listState.scrollToItem(0)
                            showToast("onTap")
                        }
                    }
                )
            })

        RefreshList(lazyPagingItems = recommendData, listState = listState, isRefreshing = isRefreshing, onRefresh = {
            viewModel.dispatch(ViewAction.Refresh)
        }) {
            val step = 3
            for (index in 0 until recommendData.itemCount step step) {
                stickyHeader {
                    Text(text = index.toString(),
                        color = AppTheme.colors.textPrimary,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = AppTheme.colors.primary)
                            .padding(10.dp))
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val modifier = Modifier
                            .fillParentMaxWidth(1 / step.toFloat())
                            .padding(10.dp, 10.dp)
                            .border(BorderStroke(1.dp, AppTheme.colors.secondary))
                            .padding(10.dp, 10.dp)
                        for (i in 0 until step)
                            if (index + i < recommendData.itemCount)
                                Text(text = recommendData[index + i]?.Name ?: "",
                                    color = AppTheme.colors.textPrimary,
                                    modifier = modifier,
                                    textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}
