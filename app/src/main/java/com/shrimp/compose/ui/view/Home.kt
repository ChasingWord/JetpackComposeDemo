package com.shrimp.compose.ui.view

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shrimp.compose.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by chasing on 2022/3/22.
 */
@Preview
@Composable
fun HomePreview() {
    Home()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Home() {
    Column(verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        val navController = rememberAnimatedNavController()
        AnimatedNavHost(
            navController = navController, startDestination = "home",
            modifier = Modifier.weight(1f), route = "main",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(1000),//动画时长1s
                    initialOffsetX = {
                        -it//初始位置在正一屏的位置，也就是说初始位置我们看不到，动画动起来的时候会从正一屏位置滑动到屏幕位置
                    })
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(1000), targetOffsetX = {
                    it
                })
            }
        ) {
            composable("home") { HomeMain() }
            composable("community", ) { HomeCommunity() }
            composable(route = "message") {
                Text(text = "消息",
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .fillMaxWidth()
                        .padding(50.dp),
                    fontSize = 17.sp,
                    color = colorResource(id = R.color.color_2c2c2c))
            }
            composable("mine") { HomeMine(hadSign = MutableLiveData(false)) }
        }
        Divider(
            color = colorResource(id = R.color.color_f1f3f5),
            //线的高度
            thickness = 1.dp
        )
        HomeNavBar(navController)
    }
}

@Composable
fun HomeNavBar(navController: NavController) {
    var position by remember { mutableStateOf(0) }
    Row(modifier = Modifier.navigationBarsPadding()) {
        val titleList = listOf("首页", "社区", "消息", "我的")
        val idList = listOf(R.mipmap.main_bottom_tab_home_normal,
            R.mipmap.main_bottom_tab_community_normal,
            R.mipmap.main_bottom_tab_message_normal,
            R.mipmap.main_bottom_tab_mine_normal)
        val modifier = Modifier
            .weight(1f)
            .background(color = colorResource(id = R.color.transparent))
        for (index in titleList.indices) {
            HomeNavBarSingle(modifier = modifier, resId = idList[index],
                name = titleList[index], isChecked = index == position) {
                if (position != index) {
                    val builder: NavOptionsBuilder.() -> Unit = {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        val tag: String = when (position) {
                            0 -> "home"
                            1 -> "community"
                            2 -> "message"
                            3 -> "mine"
                            else -> ""
                        }
                        // 同级跳转，使当前的退出回退栈，避免点击回退键返回当前视图
                        popUpTo(navController.graph[tag].id) {
                            inclusive = true
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                    when (index) {
                        0 -> navController.navigate("home", builder)
                        1 -> navController.navigate("community", builder)
                        2 -> navController.navigate("message", builder)
                        3 -> navController.navigate("mine", builder)
                    }
                    position = index
                } else {
                    when (index) {
                        0 -> CoroutineScope(Dispatchers.Main).launch {
                            HomeMainTotalScroll.value = 0f
                            HomeMainScrollState?.scrollToItem(0, 0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeNavBarSingle(
    modifier: Modifier, resId: Int, name: String, isChecked: Boolean,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick, modifier) {
        Column(horizontalAlignment = CenterHorizontally) {
            Icon(painter = painterResource(id = resId), contentDescription = "底部标签",
                tint = colorResource(id = if (isChecked) R.color.color_ff609d else R.color.color_aeaeae))
            Text(text = name, fontSize = 13.sp,
                color = colorResource(id = R.color.color_2c2c2c),
                modifier = Modifier
                    .padding(0.dp, 3.dp, 0.dp, 0.dp))
        }
    }
}