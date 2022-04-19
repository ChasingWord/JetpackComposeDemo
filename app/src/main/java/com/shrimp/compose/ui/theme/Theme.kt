package com.shrimp.compose.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.*

var LocalAppColors = compositionLocalOf {
    lightColors()
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
//    MaterialTheme {
//        content.invoke()
//    }
    CompositionLocalProvider(LocalAppColors provides getColors(), content = content)
}

@Stable
object AppTheme {
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = getColors()

    val typography: androidx.compose.material.Typography
        @Composable
        @ReadOnlyComposable
        get() = Typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = Shapes
}

