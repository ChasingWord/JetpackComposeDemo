package com.shrimp.compose.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shrimp.compose.util.GenericTools
import com.shrimp.compose.R
import com.shrimp.compose.bean.TOPIC_DATA_TYPE_IMAGE
import com.shrimp.compose.bean.TOPIC_DATA_TYPE_NEWS
import com.shrimp.compose.bean.TOPIC_DATA_TYPE_RESOURCE
import com.shrimp.compose.bean.TopicData

/**
 * Created by chasing on 2022/3/23.
 */
@Preview
@Composable
fun TopicItemPreview() {
    TopicItem(TopicData(0, false))
}

@Composable
fun TopicItem(topicData: TopicData) {
    Box {
        Column {
            TopicItemUserInfo(topicData = topicData)
            Text(text = "内容.....",
                fontSize = 15.sp,
                color = colorResource(id = R.color.color_282a2e),
                modifier = Modifier.padding(13.dp, 12.dp, 13.dp, 0.dp))
            when (topicData.type) {
                TOPIC_DATA_TYPE_IMAGE -> TopicItemImgInfo(topicData.imgList)
                TOPIC_DATA_TYPE_RESOURCE -> TopicItemResource()
                TOPIC_DATA_TYPE_NEWS -> TopicItemNews()
            }
            TopicItemLabel()
            TopicItemFunction()
            Divider(color = colorResource(id = R.color.color_f1f3f5))
        }
    }
}

@Composable
fun TopicItemUserInfo(topicData: TopicData) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(13.dp, 20.dp, 13.dp, 0.dp)) {
        Image(modifier = Modifier
            .size(31.dp)
            .clip(CircleShape),
            painter = painterResource(id = R.mipmap.default_pic), contentDescription = null)
        Column(modifier = Modifier
            .weight(1f)
            .padding(10.dp, 0.dp)) {
            Row {
                Text(text = "昵称",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.color_282a2e))
                Image(painter = painterResource(id = R.mipmap.lv_1),
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp))
            }
            Text(text = "2022-3-24",
                fontSize = 10.sp,
                color = colorResource(id = R.color.color_bbbec4))
        }
        if (!topicData.isFocus) {
            Text(text = "+ 关注",
                fontSize = 12.sp,
                color = colorResource(id = R.color.color_ff609d),
                modifier = Modifier
                    .border(1.dp,
                        color = colorResource(id = R.color.color_ff609d),
                        shape = RoundedCornerShape(5.dp))
                    .padding(10.dp, 4.dp)
                    .clickable { topicData.isFocus = true })
        }
    }
}

@Composable
fun TopicItemImgInfo(imgList: List<String>) {
    if (imgList.isNotEmpty()) {
        val countOfSingleRow = if (imgList.size == 4) 2 else 3
        val screenWidth = GenericTools.getScreenWidth(LocalContext.current)
        var width: Int
        with(LocalDensity.current) {
            width = GenericTools.px2dip(LocalContext.current, screenWidth - 34.dp.toPx())
        }
        for (index in 0..imgList.size / countOfSingleRow) {
            Row(Modifier.padding(13.dp, (if (index == 0) 10.dp else 4.dp), 13.dp, 0.dp)) {
                for (i in 0 until countOfSingleRow) {
                    TopicItemImgSingle(imgUrl = imgList[index * countOfSingleRow + i],
                        modifier = Modifier
                            .padding((if (index == 0) 0.dp else 4.dp),
                                0.dp,
                                0.dp,
                                0.dp)
                            .width(width.dp)
                            .aspectRatio(1f))
                }
            }
        }
    }
}

@Composable
fun TopicItemImgSingle(imgUrl: String, modifier: Modifier) {
    AsyncImage(model = imgUrl, contentDescription = null,
        modifier = modifier.clip(RoundedCornerShape(4.dp)))
}

@Composable
fun TopicItemResource() {
    Row(modifier = Modifier
        .padding(13.dp, 10.dp, 13.dp, 0.dp)
        .height(IntrinsicSize.Max)
        .background(color = colorResource(id = R.color.color_f6f8fa),
            shape = RoundedCornerShape(4.dp))) {
        Box {
            Image(painter = painterResource(id = R.mipmap.default_pic_deep),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(131.dp)
                    .aspectRatio(131 / 96f)
                    .clip(RoundedCornerShape(4.dp, 0.dp, 0.dp, 4.dp)))
            Text(text = "84MB", fontSize = 10.sp, color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(4.dp, 0.dp, 0.dp, 5.dp)
                    .background(colorResource(id = R.color.p50_transition),
                        shape = RoundedCornerShape(4.dp))
                    .padding(3.dp, 2.dp)
                    .align(Alignment.BottomStart))
        }
        Column(modifier = Modifier
            .padding(10.dp, 10.dp, 10.dp, 0.dp)
            .fillMaxHeight()) {
            Text(text = "资源标题--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                maxLines = 2,
                fontSize = 15.sp,
                color = colorResource(id = R.color.color_282a2e),
                modifier = Modifier.defaultMinSize(0.dp, 42.dp))
            Row(modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 9.dp)
                .fillMaxHeight(), verticalAlignment = Alignment.Bottom) {
                Text(text = "销量：46",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.color_bbbec4),
                    modifier = Modifier
                        .weight(1f))
                Text(text = "2",
                    fontSize = 19.sp,
                    color = colorResource(id = R.color.color_ff609d),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(2.dp, 0.dp))
                Text(text = "币", fontSize = 12.sp, color = colorResource(id = R.color.color_ff609d))
            }
        }
    }
}

@Composable
fun TopicItemNews() {
    Column(modifier = Modifier
        .padding(13.dp, 10.dp, 13.dp, 0.dp)
        .background(color = colorResource(id = R.color.color_f6f8fa),
            shape = RoundedCornerShape(4.dp))
        .padding(13.dp, 0.dp, 13.dp, 10.dp)) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(0.dp, 10.dp, 10.dp, 0.dp)) {
                Text(text = "假如给你1个亿？",
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.color_282a2e),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth())
                Text(text = "最简单稳定的操作方式，做一个一天期的国债逆回购。具体操...",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.color_a6a9ad),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth())
            }
            Image(painter = painterResource(id = R.mipmap.default_pic_deep),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(0.dp, 13.dp, 0.dp, 0.dp)
                    .width(126.dp)
                    .aspectRatio(126 / 80f)
                    .clip(RoundedCornerShape(4.dp)))
        }
        Row(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
            Text(text = "1323 阅读",
                fontSize = 12.sp,
                color = colorResource(id = R.color.color_bbbec4),
                modifier = Modifier.weight(1f))
            Text(text = "2021-08-30",
                fontSize = 12.sp,
                color = colorResource(id = R.color.color_bbbec4))
        }
    }
}

@Composable
fun TopicItemLabel() {
    Row(modifier = Modifier.padding(13.dp, 10.dp, 13.dp, 0.dp)) {
        Text(text = "标签", fontSize = 12.sp, color = colorResource(id = R.color.color_4b5057),
            modifier = Modifier
                .border(1.dp, colorResource(id = R.color.color_p10282a2e), RoundedCornerShape(4.dp))
                .padding(10.dp, 5.dp))
        Box(Modifier.width(10.dp))
        Text(text = "标签", fontSize = 12.sp, color = colorResource(id = R.color.color_4b5057),
            modifier = Modifier
                .border(1.dp, colorResource(id = R.color.color_p10282a2e), RoundedCornerShape(4.dp))
                .padding(10.dp, 5.dp))
    }
}

@Composable
fun TopicItemFunction() {
    val context = LocalContext.current
    Row(modifier = Modifier.height(62.dp)) {
        TextButton(onClick = { Toast.makeText(context, "转发", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()) {
            Image(painter = painterResource(id = R.mipmap.forward), contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
            Text(text = "转发", fontSize = 13.sp, color = colorResource(id = R.color.color_282a2e))
        }
        TextButton(onClick = { Toast.makeText(context, "评论", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()) {
            Image(painter = painterResource(id = R.mipmap.comment), contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
            Text(text = "评论", fontSize = 13.sp, color = colorResource(id = R.color.color_282a2e))
        }
        TextButton(onClick = { Toast.makeText(context, "奶一口", Toast.LENGTH_SHORT).show() },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()) {
            Image(painter = painterResource(id = R.mipmap.praise), contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
            Text(text = "奶一口", fontSize = 13.sp, color = colorResource(id = R.color.color_282a2e))
        }
    }
}