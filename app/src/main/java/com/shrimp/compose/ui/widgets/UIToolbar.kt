package com.shrimp.compose.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shrimp.base.utils.RouteUtils.back
import com.shrimp.compose.R
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_transparent

/**
 * Created by chasing on 2022/4/19.
 */
private val titleTextSize = 17.sp
private val functionTextSize = 14.sp
private val titleHorizontalPadding = 60.dp

@Composable
fun Toolbar(
    navCtrl: NavHostController,
    title: String,
    backgroundColor: Color = color_transparent,
    isStatusBarPadding: Boolean = false,
) {
    val modifier: Modifier =
        if (isStatusBarPadding)
            Modifier
                .background(backgroundColor)
                .statusBarsPadding()
                .fillMaxWidth()
                .height(AppTheme.dimen.toolbarHeight)
        else
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .height(AppTheme.dimen.toolbarHeight)
    Box(modifier = modifier) {
        Image(painter = painterResource(id = R.mipmap.arrow_left_60),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(AppTheme.dimen.safeSpace, 0.dp)
                .clickable { navCtrl.back() },
            contentScale = ContentScale.Inside)

        Column(modifier = Modifier
            .align(Alignment.Center)
            .padding(titleHorizontalPadding, 0.dp)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = titleTextSize,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun Toolbar(
    navCtrl: NavHostController,
    title: String,
    function: String,
    functionClick: () -> Unit,
    backgroundColor: Color = color_transparent,
    isStatusBarPadding: Boolean = false,
) {
    val modifier: Modifier =
        if (isStatusBarPadding)
            Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .background(backgroundColor)
                .height(AppTheme.dimen.toolbarHeight)
        else
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .height(AppTheme.dimen.toolbarHeight)
    Box(modifier = modifier) {
        Image(painter = painterResource(id = R.mipmap.arrow_left_60),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(AppTheme.dimen.safeSpace, 0.dp)
                .clickable { navCtrl.back() },
            contentScale = ContentScale.Inside)

        Column(modifier = Modifier
            .align(Alignment.Center)
            .padding(titleHorizontalPadding, 0.dp)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = titleTextSize,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        Row(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(AppTheme.dimen.safeSpace)) {
            Text(text = function, color = AppTheme.colors.textPrimary, fontSize = functionTextSize,
                modifier = Modifier.clickable { functionClick.invoke() })
        }
    }
}

@Composable
fun Toolbar(
    navCtrl: NavHostController,
    title: String,
    functionIcon: Int,
    functionClick: () -> Unit,
    backgroundColor: Color = color_transparent,
    isStatusBarPadding: Boolean = false,
) {
    val modifier: Modifier =
        if (isStatusBarPadding)
            Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .background(backgroundColor)
                .height(AppTheme.dimen.toolbarHeight)
        else
            Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .height(AppTheme.dimen.toolbarHeight)
    Box(modifier = modifier) {
        Image(painter = painterResource(id = R.mipmap.arrow_left_60),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(AppTheme.dimen.safeSpace, 0.dp)
                .clickable { navCtrl.back() },
            contentScale = ContentScale.Inside)

        Column(modifier = Modifier
            .align(Alignment.Center)
            .padding(titleHorizontalPadding, 0.dp)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = titleTextSize,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        Row(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(AppTheme.dimen.safeSpace)) {
            Image(painter = painterResource(id = functionIcon),
                contentDescription = null,
                modifier = Modifier.clickable { functionClick.invoke() })
        }
    }
}