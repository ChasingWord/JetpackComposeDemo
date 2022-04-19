package com.shrimp.compose.screen.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.shrimp.compose.R

sealed class BottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    var icon: Int,
) {
    object Home : BottomNavRoute(RouteName.HOME, R.string.home, R.mipmap.main_bottom_tab_home_normal)
    object Category : BottomNavRoute(RouteName.COMMUNITY, R.string.community, R.mipmap.main_bottom_tab_community_normal)
    object Collection : BottomNavRoute(RouteName.MESSAGE, R.string.message, R.mipmap.main_bottom_tab_message_normal)
    object Profile : BottomNavRoute(RouteName.MINE, R.string.mine, R.mipmap.main_bottom_tab_mine_normal)
}