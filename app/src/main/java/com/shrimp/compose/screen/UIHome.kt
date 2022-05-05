package com.shrimp.compose.screen

import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.shrimp.base.utils.L
import com.shrimp.base.utils.RouteUtils
import com.shrimp.base.widgets.composableWithDefaultAnim
import com.shrimp.compose.common.RouteName
import com.shrimp.compose.common.bus_event.EventScrollToTop
import com.shrimp.compose.screen.main.ui.*
import com.shrimp.compose.screen.webview.WebPager
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_a6a9ad
import com.shrimp.compose.ui.theme.color_ff609d
import com.shrimp.compose.ui.widgets.AppSnackBar
import org.greenrobot.eventbus.EventBus

/**
 * Created by chasing on 2022/4/19.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Home(activity: ComponentActivity) {
    val navCtrl = rememberAnimatedNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()

    Scaffold(modifier = Modifier.background(AppTheme.colors.primary),
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
                startDestination = RouteName.HOME,
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, it.calculateBottomPadding())
                    .fillMaxWidth()
                    .fillMaxHeight(),
                route = RouteName.NAV_ROOT,
                enterTransition = {
                    fadeIn(animationSpec = tween(1))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(1))
                },
            ) {
                composableWithDefaultAnim(RouteName.HOME) {
                    HomeMain(navCtrl, scaffoldState)
                }
                composableWithDefaultAnim(RouteName.COMMUNITY) {
                    HomeCommunity(navCtrl)
                }
                composableWithDefaultAnim(RouteName.MESSAGE) {
                    Text(text = "消息",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(50.dp),
                        fontSize = 17.sp,
                        color = AppTheme.colors.textPrimary)
                }
                composableWithDefaultAnim(RouteName.MINE) {
                    HomeMine(navCtrl,
                        scaffoldState,
                        activity)
                }

                composableWithDefaultAnim(
                    "${RouteName.LABEL}/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                ) { navBackStackEntry ->
                    Label(navCtrl,
                        scaffoldState,
                        navBackStackEntry.arguments?.getInt("userId") ?: 0)
                }
                composableWithDefaultAnim(
                    "${RouteName.COMMUNITY_PERSONAL}/{userId}",
                    arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                ) { navBackStackEntry ->
                    CommunityPersonal(navCtrl,
                        scaffoldState,
                        navBackStackEntry.arguments?.getInt("userId") ?: 0)
                }
                composableWithDefaultAnim(
                    "${RouteName.WEB_VIEW}/{url}",
                    arguments = listOf(navArgument("url") { type = NavType.StringType }),
                ) { navBackStackEntry ->
                    WebPager(navCtrl,
                        scaffoldState,
                        navBackStackEntry.arguments?.getString("url") ?: "")
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { data ->
                AppSnackBar(data = data)
            }
        }
    )
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
                        RouteUtils.navTo(navCtrl,
                            screen.routeName,
                            backStackRouteName = currentDestination?.route,
                            isInclusive = true)
                    } else {
                        when (screen.routeName) {
                            RouteName.HOME -> EventBus.getDefault()
                                .post(EventScrollToTop(EventScrollToTop.TYPE_HOME_MAIN))
                            RouteName.COMMUNITY -> EventBus.getDefault()
                                .post(EventScrollToTop(EventScrollToTop.TYPE_HOME_COMMUNITY))
                        }
                    }
                })
        }
    }
}