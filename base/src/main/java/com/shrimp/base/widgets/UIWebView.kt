package com.shrimp.base.widgets

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * Created by chasing on 2022/4/29.
 */
@SuppressLint("SetJavaScriptEnabled")
fun WebView.initSettings() {
    requestFocus() //如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件

//        mContentWv.setInitialScale(1); //设置页面加载比例，1代表直接将整个网页加载到界面上，会自动缩放进行适应到屏幕尺寸
    //        mContentWv.setInitialScale(1); //设置页面加载比例，1代表直接将整个网页加载到界面上，会自动缩放进行适应到屏幕尺寸
//        settings.setTextZoom(100);//设置WebView中加载页面字体变焦百分比，默认100。//嵌入一个H5页面，使H5页面的字体不会随用户自己调整的系统字体变化而变化
    //        settings.setTextZoom(100);//设置WebView中加载页面字体变焦百分比，默认100。//嵌入一个H5页面，使H5页面的字体不会随用户自己调整的系统字体变化而变化
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.defaultTextEncodingName = "UTF-8"
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.blockNetworkImage = false //解决图片不显示

    settings.loadsImagesAutomatically = true
    // 根据 HTTP 协议头里的 Cache-Control（或 Expires）和 Last-Modified（或 Etag）等字段来控制文件缓存的机制，
    // 现在设置的缓存方式是根据网页设置的这几个值判断的，没有过期的话就会读取本地缓存，过期了则进行重新加载
    // 本地缓存在内存不足的时候也会被清理，在网络请求失败的时候显示错误页面
    // 根据 HTTP 协议头里的 Cache-Control（或 Expires）和 Last-Modified（或 Etag）等字段来控制文件缓存的机制，
    // 现在设置的缓存方式是根据网页设置的这几个值判断的，没有过期的话就会读取本地缓存，过期了则进行重新加载
    // 本地缓存在内存不足的时候也会被清理，在网络请求失败的时候显示错误页面
    settings.cacheMode = WebSettings.LOAD_DEFAULT
    settings.useWideViewPort = true // 这个很关键，设置webview推荐使用的窗口

    settings.loadWithOverviewMode = true // 设置webview加载的页面的模式，也设置为true

    settings.displayZoomControls = false // 隐藏webview缩放按钮

    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL //解决字符串换行问题

    settings.mediaPlaybackRequiresUserGesture = false
    settings.builtInZoomControls = false //关闭缩放功能support zoom

    settings.setSupportZoom(false)

    //允许混合加载，即http与https可以同时加载，避免加载的是https的url，而里面的图片视频使用的是http的情况

    //允许混合加载，即http与https可以同时加载，避免加载的是https的url，而里面的图片视频使用的是http的情况
    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
}