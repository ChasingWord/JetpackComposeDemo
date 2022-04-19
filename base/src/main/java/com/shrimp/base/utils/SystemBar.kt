package com.shrimp.base.utils

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

fun setDecorFitsSystemWindows(window: Window){
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

/**
 * Created by chasing on 2022/3/24.
 * 添加SideEffect避免一直重复执行
 */
@Composable
fun SystemStatusBarChangeColor(color: Color, isShowDarkIcon: Boolean) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // Update the system bars to be translucent
        systemUiController.setStatusBarColor(color, isShowDarkIcon)
    }
}

/**
 * 需要在setContent之前调用：
 * WindowCompat.setDecorFitsSystemWindows(window, false)
 */
@Composable
fun SystemStatusBarTransparent(isShowDarkIcon: Boolean) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // Update the system bars to be translucent
        systemUiController.setStatusBarColor(Color.Transparent, isShowDarkIcon)
    }
}

@Composable
fun SystemHideStatusBar() {
    val view = LocalView.current
    SideEffect {
        // Update the system bars to be translucent
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.let {
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsetsCompat.Type.statusBars())
        }
    }
}

@Composable
fun SystemFullScreen() {
    val view = LocalView.current
    SideEffect {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.let {
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            it.hide(WindowInsetsCompat.Type.statusBars())
            it.hide(WindowInsetsCompat.Type.navigationBars())
        }
    }
}

