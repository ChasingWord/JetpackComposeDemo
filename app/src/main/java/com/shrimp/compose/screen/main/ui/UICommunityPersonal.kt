package com.shrimp.compose.screen.main.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrimp.compose.R

/**
 * Created by chasing on 2022/4/15.
 */
@Composable
fun CommunityPersonal(userId: Int) {
    Text(text = "个人", fontSize = 13.sp, color = colorResource(id = R.color.color_282a2e),
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(20.dp, 20.dp))
}