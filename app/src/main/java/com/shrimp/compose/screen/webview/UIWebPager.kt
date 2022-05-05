package com.shrimp.compose.screen.webview

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.base.widgets.initSettings
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.widgets.Toolbar

/**
 * Created by chasing on 2022/4/29.
 */
@Composable
fun WebPager(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState,
    url: String,
) {
    SystemStatusBarTransparent(isShowDarkIcon = true)
    val webViewState = rememberWebViewState(url = url)
    var isLoadError by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    Column {
        Toolbar(navCtrl = navHostController,
            title = webViewState.pageTitle ?: "",
            backgroundColor = AppTheme.colors.primary,
            isStatusBarPadding = true)
        Box {
            if (!isLoadError)
                WebView(modifier = Modifier.fillMaxSize(),
                    state = webViewState,
                    onCreated = { it.initSettings() },
                    onError = { request: WebResourceRequest?, error: WebResourceError? ->
                        val requestUrl = request?.url.toString()
                        val errorUrl = webViewState.content.getCurrentUrl().toString()
                        if (requestUrl.contains(errorUrl)) {
                            errorMsg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                error?.description?.toString() ?: "网络异常"
                            } else
                                "网络异常"
                            isLoadError = true
                        }
                    })
            else  {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppTheme.colors.primary),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "网页无法打开", color = AppTheme.colors.textPrimary, fontSize = 15.sp,
                        modifier = Modifier.padding(AppTheme.dimen.safeSpace,
                            40.dp,
                            AppTheme.dimen.safeSpace,
                            0.dp))
                    Text(text = "网页无法加载，因为：", color = AppTheme.colors.textPrimary, fontSize = 15.sp,
                        modifier = Modifier.padding(AppTheme.dimen.safeSpace,
                            20.dp,
                            AppTheme.dimen.safeSpace,
                            0.dp))
                    Text(text = errorMsg, color = AppTheme.colors.textPrimary, fontSize = 15.sp,
                        modifier = Modifier.padding(AppTheme.dimen.safeSpace,
                            10.dp,
                            AppTheme.dimen.safeSpace,
                            0.dp))
                    Text(text = "点击重试", color = AppTheme.colors.textPrimary, fontSize = 15.sp,
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 0.dp, 0.dp)
                            .border(1.dp,
                                color = AppTheme.colors.background,
                                shape = RoundedCornerShape(4.dp))
                            .padding(10.dp, 4.dp)
                            .clickable {
                                isLoadError = false
                            })
                }
            }
        }
    }
}