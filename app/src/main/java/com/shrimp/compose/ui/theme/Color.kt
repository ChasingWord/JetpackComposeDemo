package com.shrimp.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

val color_ffffff = Color(0xffffffff)
val color_f6f8fa = Color(0xfff6f8fa)
val color_a6a9ad = Color(0xffa6a9ad)

val color_000000 = Color(0xff000000)
val color_282a2e = Color(0xff282a2e)

val color_d87831 = Color(0xffd87831)

val color_ff0000 = Color(0xffff0000)
val color_ff609d = Color(0xffff609d)

@Stable
class Colors(
    primary: Color,
    secondary: Color,
    background: Color,
    divider: Color,
    textPrimary: Color,
    textSecondary: Color,
    confirm: Color,
    info: Color,
    success: Color,
    warn: Color,
    error: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var divider by  mutableStateOf(divider, structuralEqualityPolicy())
        internal set
    var textPrimary by  mutableStateOf(textPrimary, structuralEqualityPolicy())
        internal set
    var textSecondary by  mutableStateOf(textSecondary, structuralEqualityPolicy())
        internal set
    var confirm by  mutableStateOf(confirm, structuralEqualityPolicy())
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
    secondary: Color = color_a6a9ad,
    background: Color = color_ffffff,
    divider: Color = color_a6a9ad,
    textPrimary: Color = color_282a2e,
    textSecondary: Color= color_a6a9ad,
    confirm: Color= color_ff609d,
    info: Color= color_a6a9ad,
    success: Color= color_a6a9ad,
    warn: Color= color_a6a9ad,
    error: Color = color_a6a9ad,
): Colors = Colors(
    primary = primary,
    secondary = secondary,
    background = background,
    divider = divider,
    textPrimary = textPrimary,
    textSecondary= textSecondary,
    confirm= confirm,
    info= info,
    success= success,
    warn= warn,
    error = error,
    isLight = true
)

fun darkColors(
    primary: Color = color_000000,
    secondary: Color = color_000000,
    background: Color = color_000000,
    divider: Color = color_a6a9ad,
    textPrimary: Color = color_ffffff,
    textSecondary: Color= color_a6a9ad,
    confirm: Color= color_ff609d,
    info: Color= color_a6a9ad,
    success: Color= color_a6a9ad,
    warn: Color= color_a6a9ad,
    error: Color = color_a6a9ad,
): Colors = Colors(
    primary = primary,
    secondary = secondary,
    background = background,
    divider = divider,
    textPrimary = textPrimary,
    textSecondary= textSecondary,
    confirm= textSecondary,
    info= info,
    success= success,
    warn= warn,
    error = error,
    isLight = false
)

@Composable
@ReadOnlyComposable
fun getColors(isDarkTheme: Boolean = isSystemInDarkTheme()) = if (isDarkTheme){
    darkColors()
} else {
    lightColors()
}
