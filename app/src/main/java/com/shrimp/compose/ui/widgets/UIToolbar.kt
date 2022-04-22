package com.shrimp.compose.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shrimp.compose.R
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.base.utils.RouteUtils.back

/**
 * Created by chasing on 2022/4/19.
 */
private val toolbarHeight = 44.dp
private val titleTextSize = 17.sp
private val functionTextSize = 14.sp

@Composable
fun Toolbar(navCtrl: NavHostController, title: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(toolbarHeight)) {
        Image(painter = painterResource(id = R.mipmap.arrow_left_60),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(AppTheme.dimen.safeSpace, 0.dp)
                .clickable { navCtrl.back() },
            contentScale = ContentScale.Inside)

        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = titleTextSize)
        }
    }
}

@Composable
fun Toolbar(
    navCtrl: NavHostController,
    title: String,
    function: String,
    functionClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(toolbarHeight)) {
        Image(painter = painterResource(id = R.mipmap.arrow_left_60),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(AppTheme.dimen.safeSpace, 0.dp)
                .clickable { navCtrl.back() },
            contentScale = ContentScale.Inside)

        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = titleTextSize)
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
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(toolbarHeight)) {
        Image(painter = painterResource(id = R.mipmap.arrow_left_60),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .padding(AppTheme.dimen.safeSpace, 0.dp)
                .clickable { navCtrl.back() },
            contentScale = ContentScale.Inside)

        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = title, color = AppTheme.colors.textPrimary, fontSize = titleTextSize)
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