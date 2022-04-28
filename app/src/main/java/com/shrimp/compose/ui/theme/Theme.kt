package com.shrimp.compose.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.material.Shapes
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*

var LocalAppColors = compositionLocalOf {
    lightColors()
}

@Composable
fun AppThemeLayout(
    content: @Composable () -> Unit,
) {
    val rippleIndication = rememberRipple()
    CompositionLocalProvider(LocalAppColors provides getColors(),
        LocalIndication provides rippleIndication,
        content = content)
}

/**
 * colors会跟随主题夜间模式切换颜色，所以需要特殊处理成动态的
 * 其它三个则是固定的写死的
 */
@Stable
object AppTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: androidx.compose.material.Typography
        @Composable
        @ReadOnlyComposable
        get() = Typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = Shapes

    val dimen: Dimen
        @Composable
        @ReadOnlyComposable
        get() = Dimen()
}

