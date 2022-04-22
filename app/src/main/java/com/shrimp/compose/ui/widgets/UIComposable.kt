package com.shrimp.compose.ui.widgets

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

/**
 * Created by chasing on 2022/4/21.
 * 添加默认的界面切换动画，进行判断：
 * 同一个父节点的compose看作是在同一个界面内，不设置界面切换动画
 */
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableWithDefaultAnim(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            if (this.initialState.destination.parent?.route?.equals(this.targetState.destination.parent?.route) == false)
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = {
                    it//初始位置在正一屏的位置，也就是说初始位置我们看不到，动画动起来的时候会从正一屏位置滑动到屏幕位置
                })
            else {
                null
            }
        },
        exitTransition = {
            if (this.targetState.destination.parent?.route?.equals(this.initialState.destination.parent?.route) == false)
                slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {
                    -it
                })
            else {
                null
            }
        },
        popEnterTransition = {
            if (this.initialState.destination.parent?.route?.equals(this.targetState.destination.parent?.route) == false)
                slideInHorizontally(animationSpec = tween(500), initialOffsetX = {
                    -it//初始位置在正一屏的位置，也就是说初始位置我们看不到，动画动起来的时候会从正一屏位置滑动到屏幕位置
                })
            else {
                null
            }
        },
        popExitTransition = {
            if (this.targetState.destination.parent?.route?.equals(this.initialState.destination.parent?.route) == false)
                slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {
                    it
                })
            else {
                null
            }
        },
        content = content
    )
}