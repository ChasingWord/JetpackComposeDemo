package com.shrimp.compose.screen.main.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.get
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shrimp.base.utils.L
import com.shrimp.base.view.BaseActivity
import com.shrimp.compose.screen.common.BottomNavRoute
import com.shrimp.compose.screen.common.RouteName
import com.shrimp.compose.screen.main.HomeVMManager
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_a6a9ad
import com.shrimp.compose.ui.theme.color_ff609d

/**
 * Created by chasing on 2022/4/19.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Home(vmManager: HomeVMManager, activity: BaseActivity) {
    val navCtrl = rememberAnimatedNavController()
//    // 注册生命周期回调
//    topNavController.setLifecycleOwner(activity);
//    // 注册返回事件分发器
//    val dispatcher = OnBackPressedDispatcher {
//        activity.onBackPressedDispatcher
//    }
//    topNavController.setOnBackPressedDispatcher(dispatcher)
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()

    Scaffold(modifier = Modifier,
        bottomBar = {
            when (currentDestination?.route) {
                RouteName.HOME -> BottomNavBarView(navCtrl = navCtrl)
                RouteName.COMMUNITY -> BottomNavBarView(navCtrl = navCtrl)
                RouteName.MESSAGE -> BottomNavBarView(navCtrl = navCtrl)
                RouteName.MINE -> BottomNavBarView(navCtrl = navCtrl)
            }
        },
        content = {
            AnimatedNavHost(
                navController = navCtrl,
                startDestination = "home",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                route = "root",
            ) {
                composable("home") { HomeMain(navCtrl) }
                composable("community") { HomeCommunity(navCtrl) }
                composable("message") {
                    Text(text = "消息",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(50.dp),
                        fontSize = 17.sp,
                        color = colorResource(id = com.shrimp.compose.R.color.color_2c2c2c))
                }
                composable("mine") { HomeMine(vmManager.vmHomeMine, activity) }
                composable(
                    "community_personal/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                    enterTransition = {
                        slideInHorizontally(animationSpec = tween(500), initialOffsetX = {
                            it//初始位置在正一屏的位置，也就是说初始位置我们看不到，动画动起来的时候会从正一屏位置滑动到屏幕位置
                        })
                    },
                    exitTransition = {
                        slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {
                            -it
                        })
                    },
                    popEnterTransition = {
                        slideInHorizontally(animationSpec = tween(500), initialOffsetX = {
                            -it//初始位置在正一屏的位置，也就是说初始位置我们看不到，动画动起来的时候会从正一屏位置滑动到屏幕位置
                        })
                    },
                    popExitTransition = {
                        slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {
                            it
                        })
                    },
                ) { navBackStackEntry ->
                    CommunityPersonal(navBackStackEntry.arguments?.getInt("userId")?: 0)
                }
            }
        })
}

@Composable
fun BottomNavBarView(navCtrl: NavHostController) {
    val bottomNavList = listOf(
        BottomNavRoute.Home,
        BottomNavRoute.Category,
        BottomNavRoute.Collection,
        BottomNavRoute.Profile
    )
    BottomNavigation {

        val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavList.forEach { screen ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == screen.routeName } == true
            BottomNavigationItem(
                modifier = Modifier.background(Color.White),
                icon = {
                    Icon(
                        painterResource(id = screen.icon),
                        tint = if (selected) color_ff609d else color_a6a9ad,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(screen.stringId),
                        color = AppTheme.colors.textPrimary)
                },
                selected = selected,
                onClick = {
                    L.i("BottomNavView当前路由 ===> ${currentDestination?.hierarchy?.toList()}")
                    L.i("当前路由栈 ===> ${navCtrl.graph.nodes}")
                    if (currentDestination?.route != screen.routeName) {
                        navCtrl.navigate(screen.routeName) {
                            popUpTo(navCtrl.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}