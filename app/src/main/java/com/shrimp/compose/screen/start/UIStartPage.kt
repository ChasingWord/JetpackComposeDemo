package com.shrimp.compose.screen.start

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.shrimp.base.utils.SystemFullScreen
import com.shrimp.base.utils.SystemShowSystemBar
import com.shrimp.compose.R
import com.shrimp.compose.screen.Home
import kotlinx.coroutines.delay

/**
 * Created by chasing on 2022/4/22.
 */
@Composable
fun StartPage(activity: ComponentActivity) {
    var isStarting by remember { mutableStateOf(true) }
    if (isStarting) {
        Start()
        LaunchedEffect(Unit) {
            delay(500)
            isStarting = false
        }
    } else {
        SystemShowSystemBar()
        Home(activity = activity)
    }
}

@Composable
fun Start() {
    SystemFullScreen()
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.mipmap.start_info), contentDescription = null)
    }
}