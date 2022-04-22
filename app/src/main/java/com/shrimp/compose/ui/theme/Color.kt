package com.shrimp.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val color_ffffff = Color(0xffffffff)
val color_f6f8fa = Color(0xfff6f8fa)
val color_f5f5f5 = Color(0xfff5f5f5)
val color_f1f3f5 = Color(0xfff1f3f5)
val color_a6a9ad = Color(0xffa6a9ad)
val color_31353a = Color(0xff31353a)
val color_282a2e = Color(0xff282a2e)
val color_000000 = Color(0xff000000)

val color_ff609d = Color(0xffff609d)

@Stable
class Colors(
    primary: Color, //主色，背景色
    secondary: Color, //次色，次选背景颜色
    background: Color, //按钮等背景颜色
    divider: Color, //间隔线颜色
    textPrimary: Color, ///字体主色
    textSecondary: Color, //字体次色
    confirm: Color, //确认颜色
    info: Color, //提示颜色
    success: Color, //成功颜色
    warn: Color, //警告提示颜色
    error: Color, //错误提示颜色
    isLight: Boolean, //是否是亮色主题
) {
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var divider by mutableStateOf(divider, structuralEqualityPolicy())
        internal set
    var textPrimary by mutableStateOf(textPrimary, structuralEqualityPolicy())
        internal set
    var textSecondary by mutableStateOf(textSecondary, structuralEqualityPolicy())
        internal set
    var confirm by mutableStateOf(confirm, structuralEqualityPolicy())
        internal set

    var info by mutableStateOf(info, structuralEqualityPolicy())
        internal set
    var success by mutableStateOf(success, structuralEqualityPolicy())
        internal set
    var warn by mutableStateOf(warn, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set

    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
        internal set
}

fun lightColors(
    primary: Color = color_ffffff,
    secondary: Color = color_f5f5f5,
    background: Color = color_f6f8fa,
    divider: Color = color_f6f8fa,
    textPrimary: Color = color_282a2e,
    textSecondary: Color = color_a6a9ad,
    confirm: Color = color_ff609d,
    info: Color = color_a6a9ad,
    success: Color = color_a6a9ad,
    warn: Color = color_a6a9ad,
    error: Color = color_a6a9ad,
): Colors = Colors(
    primary = primary,
    secondary = secondary,
    background = background,
    divider = divider,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    confirm = confirm,
    info = info,
    success = success,
    warn = warn,
    error = error,
    isLight = true
)

fun darkColors(
    primary: Color = color_000000,
    secondary: Color = color_000000,
    background: Color = color_f6f8fa,
    divider: Color = color_a6a9ad,
    textPrimary: Color = color_ffffff,
    textSecondary: Color = color_a6a9ad,
    confirm: Color = color_ff609d,
    info: Color = color_a6a9ad,
    success: Color = color_a6a9ad,
    warn: Color = color_a6a9ad,
    error: Color = color_a6a9ad,
): Colors = Colors(
    primary = primary,
    secondary = secondary,
    background = background,
    divider = divider,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    confirm = confirm,
    info = info,
    success = success,
    warn = warn,
    error = error,
    isLight = false
)

@Composable
@ReadOnlyComposable
fun getColors(isDarkTheme: Boolean = isSystemInDarkTheme()) = if (isDarkTheme) {
    darkColors()
} else {
    lightColors()
}
