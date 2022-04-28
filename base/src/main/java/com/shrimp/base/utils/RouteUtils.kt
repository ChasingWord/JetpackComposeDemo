package com.shrimp.base.utils

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * 路由名称
 */
object RouteUtils {

    const val STEAD_SYMBOL = "^0^"

    //初始化Bundle参数
    fun initBundle(params: Parcelable) = Bundle().apply { putParcelable(ARGS, params) }

    // 导航到某个页面
    fun navTo(
        navCtrl: NavHostController,
        //跳转目标compose路径(route)
        destinationName: String,
        args: Any? = null,
        /*
        需要回退的compose
        A -> B -> C   跳转C时指定回退的compose为A，则B会自动从栈内移除，点击返回键时回退到A
         */
        backStackRouteName: String? = null,
        /*
        backStackRouteName不为null的情况下，是否在跳转后将backStackRouteName对应的compose退出栈
        退出栈后按返回键不会回到该compose，
        即如果是同一级的compose则跳转时backStackRouteName传入当前加载的compose路径(route)且isInclusive设为true
        A -> B   A切换到B时传入A，且isInclusive设为true，则点击返回键时不会回退到A，而是A之前的一个compose
         */
        isInclusive:Boolean = false,
        isLaunchSingleTop: Boolean = true,
        //切换状态的时候保存页面状态
        needToRestoreState: Boolean = true,
    ) {
        var singleArgument = ""
        if (args != null) {
            when (args) {
//                is Parcelable -> {
//                    singleArgument = String.format("/%s", Uri.encode(args))
//                }
                is String -> {
                    singleArgument = String.format("/%s", Uri.encode(args))
//                    singleArgument = String.format("/%s", args)
                }
                is Int -> {
                    singleArgument = String.format("/%s", args)
                }
                is Float -> {
                    singleArgument = String.format("/%s", args)
                }
                is Double -> {
                    singleArgument = String.format("/%s", args)
                }
                is Boolean -> {
                    singleArgument = String.format("/%s", args)
                }
                is Long -> {
                    singleArgument = String.format("/%s", args)
                }
            }
        }
        println("导航到： $destinationName")
        navCtrl.navigate("$destinationName$singleArgument") {
            if (backStackRouteName != null) {
                popUpTo(backStackRouteName) {
                    saveState = true
                    inclusive = isInclusive
                }
            }
            launchSingleTop = isLaunchSingleTop
            restoreState = needToRestoreState
        }
    }

    fun NavHostController.back() {
        navigateUp()
    }

    private fun getPopUpId(navCtrl: NavHostController, routeName: String?): Int {
        val defaultId = navCtrl.graph.findStartDestination().id
        return if (routeName == null) {
            defaultId
        } else {
            navCtrl.findDestination(routeName)?.id ?: defaultId
        }
    }

    fun <T> getArguments(navCtrl: NavHostController): T? {
        return navCtrl.previousBackStackEntry?.arguments?.getParcelable(ARGS)
    }

    /**
     * 各个序列化的参数类的key名
     */
    private const val ARGS = "args"


}