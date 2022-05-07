package com.shrimp.base.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shrimp.base.R
import com.shrimp.base.utils.FileUtil

/**
 * Created by chasing on 2022/5/5.
 */
private val Error_Normal = R.drawable.default_pic

@Composable
fun ImageNet(
    url: String,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .error(errorResId)
            .build(),
        contentDescription = null)
}

@Composable
fun ImageNet(
    url: String,
    size: Dp,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier
        .size(size),
        model = url,
        error = painterResource(id = errorResId),
        contentDescription = null)
}

@Composable
fun ImageNet(
    url: String,
    shape: Shape,
    size: Dp,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier
        .clip(shape)
        .size(size),
        model = url,
        error = painterResource(id = errorResId),
        contentDescription = null)
}

@Composable
fun ImageNet(
    url: String,
    shape: Shape,
    size: Dp,
    borderWidth: Dp,
    borderColor: Color,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier
        .clip(shape)
        .border(borderWidth, borderColor, shape)
        .size(size),
        model = url,
        error = painterResource(id = errorResId),
        contentDescription = null)
}

@Composable
fun ImageFile(
    filePath: String,
    size: Dp,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier
        .size(size),
        model = FileUtil.getFileUri(LocalContext.current, filePath),
        error = painterResource(id = errorResId),
        contentDescription = null)
}

@Composable
fun ImageFile(
    filePath: String,
    shape: Shape,
    size: Dp,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier
        .clip(shape)
        .size(size),
        model = FileUtil.getFileUri(LocalContext.current, filePath),
        error = painterResource(id = errorResId),
        contentDescription = null)
}

@Composable
fun ImageFile(
    filePath: String,
    shape: Shape,
    size: Dp,
    borderWidth: Dp,
    borderColor: Color,
    errorResId: Int = Error_Normal,
) {
    AsyncImage(modifier = Modifier
        .clip(shape)
        .border(borderWidth, borderColor, shape)
        .size(size),
        model = FileUtil.getFileUri(LocalContext.current, filePath),
        error = painterResource(id = errorResId),
        contentDescription = null)
}