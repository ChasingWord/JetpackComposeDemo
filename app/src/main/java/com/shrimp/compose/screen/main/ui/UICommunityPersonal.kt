package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.shrimp.compose.engine.ViewAction
import com.shrimp.compose.screen.main.vm.VMCommunityPersonal
import com.shrimp.compose.ui.widgets.RefreshList

/**
 * Created by chasing on 2022/4/15.
 */
@Composable
fun CommunityPersonal(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    userId: Int,
    viewModel: VMCommunityPersonal = hiltViewModel(),
) {
    val viewStates = viewModel.viewStates
    val recommendData = viewStates.pagingData.collectAsLazyPagingItems()
    val isRefreshing = viewStates.isRefreshing
    val listState = if (recommendData.itemCount > 0) viewStates.listState else LazyListState()

    RefreshList(recommendData, listState = listState, isRefreshing = isRefreshing, onRefresh = {
        viewModel.dispatch(ViewAction.Refresh)
    }) {
        itemsIndexed(recommendData) { _, item ->
            Text(text = item?.Name ?: "")
        }
    }
}
