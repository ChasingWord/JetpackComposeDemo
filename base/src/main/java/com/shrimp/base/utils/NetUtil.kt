package com.shrimp.base.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * Created by chasing on 2021/10/22.
 */
object NetUtil {

    fun checkNet(context: Context): Boolean {
        // 判断是否具有可以用于通信渠道
        val mobileConnection = isMobileConnection(context)
        val wifiConnection = isWIFIConnection(context)
        return !(!mobileConnection && !wifiConnection)
    }

    /**
     * 判断手机接入点（APN）是否处于可以使用的状态
     *
     * @param context
     * @return
     */
    private fun isMobileConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * 判断当前wifi是否是处于可以使用状态
     *
     * @param context
     * @return
     */
    private fun isWIFIConnection(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * 打开网络设置界面
     */
    fun openSetting(activity: Activity) {
        val intent = Intent("/")
        val cm = ComponentName("com.android.settings", "com.android.settings.WirelessSettings")
        intent.component = cm
        intent.action = "android.intent.action.VIEW"
        activity.startActivityForResult(intent, 0)
    }
}