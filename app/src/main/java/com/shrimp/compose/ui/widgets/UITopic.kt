package com.shrimp.compose.ui.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.shrimp.base.utils.GenericTools
import com.shrimp.base.utils.RouteUtils
import com.shrimp.base.utils.showToast
import com.shrimp.base.utils.toDp
import com.shrimp.compose.R
import com.shrimp.compose.bean.TopicData
import com.shrimp.compose.common.RouteName
import com.shrimp.compose.common.bus_event.EventTopicPraise
import com.shrimp.compose.ui.theme.AppTheme
import org.greenrobot.eventbus.EventBus

/**
 * Created by chasing on 2022/3/23.
 * 类似点赞子类的功能，都通过EventBus事件通知VM进行请求刷新
 */
@Preview
@Composable
fun TopicItemPreview() {
    val navController = rememberNavController()
    TopicItem(navController, TopicData(0, 0))
}

@Composable
fun TopicItem(
    navCtrl: NavHostController,
    topicData:
    TopicData,
) {
    Column(modifier = Modifier.background(color = AppTheme.colors.primary)) {
        TopicItemUserInfo(navCtrl, topicData)
        Text(text = "内容.....",
            fontSize = 15.sp,
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(AppTheme.dimen.safeSpace,
                12.dp,
                AppTheme.dimen.safeSpace,
                0.dp))
        when (topicData.type) {
            TopicData.TOPIC_DATA_TYPE_IMAGE -> TopicItemImgInfo(topicData.imgList)
            TopicData.TOPIC_DATA_TYPE_RESOURCE -> TopicItemResource()
            TopicData.TOPIC_DATA_TYPE_NEWS -> TopicItemNews()
        }
        TopicItemLabel(navCtrl)
        TopicItemFunction(topicData)
        Divider(color = AppTheme.colors.divider)
    }
}

@Composable
fun TopicItemUserInfo(navCtrl: NavHostController, topicData: TopicData) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(AppTheme.dimen.safeSpace,
            20.dp,
            AppTheme.dimen.safeSpace,
            0.dp)) {
        Image(modifier = Modifier
            .size(31.dp)
            .clip(CircleShape)
            .clickable { RouteUtils.navTo(navCtrl, RouteName.COMMUNITY_PERSONAL, 311) },
            painter = painterResource(id = R.drawable.default_pic), contentDescription = null)
        Column(modifier = Modifier
            .weight(1f)
            .padding(10.dp, 0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "昵称",
                    fontSize = 13.sp,
                    color = AppTheme.colors.textPrimary)
                Image(painter = painterResource(id = R.drawable.lv_1),
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
        var width: Dp
        with(LocalDensity.current) {
            width = (screenWidth - 34.dp.toPx()).toDp(LocalContext.current)
        }
        for (index in 0..imgList.size / countOfSingleRow) {
            Row(Modifier.padding(AppTheme.dimen.safeSpace,
                (if (index == 0) 10.dp else 4.dp),
                AppTheme.dimen.safeSpace,
                0.dp)) {
                for (i in 0 until countOfSingleRow) {
                    TopicItemImgSingle(imgUrl = imgList[index * countOfSingleRow + i],
                        modifier = Modifier
                            .padding((if (index == 0) 0.dp else 4.dp),
                                0.dp,
                                0.dp,
                                0.dp)
                            .width(width)
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
        .padding(AppTheme.dimen.safeSpace, 10.dp, AppTheme.dimen.safeSpace, 0.dp)
        .height(IntrinsicSize.Max)
        .background(color = colorResource(id = R.color.color_f6f8fa),
            shape = RoundedCornerShape(4.dp))) {
        Box {
            Image(painter = painterResource(id = R.drawable.default_pic_deep),
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
                color = AppTheme.colors.textPrimary,
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
        .padding(AppTheme.dimen.safeSpace, 10.dp, AppTheme.dimen.safeSpace, 0.dp)
        .background(color = colorResource(id = R.color.color_f6f8fa),
            shape = RoundedCornerShape(4.dp))
        .padding(AppTheme.dimen.safeSpace, 0.dp, AppTheme.dimen.safeSpace, 10.dp)) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(0.dp, 10.dp, 10.dp, 0.dp)) {
                Text(text = "假如给你1个亿？",
                    fontSize = 15.sp,
                    color = AppTheme.colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth())
                Text(text = "最简单稳定的操作方式，做一个一天期的国债逆回购。具体操...",
                    fontSize = 12.sp,
                    color = AppTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth())
            }
            Image(painter = painterResource(id = R.drawable.default_pic_deep),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(0.dp, AppTheme.dimen.safeSpace, 0.dp, 0.dp)
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
fun TopicItemLabel(navCtrl: NavHostController) {
    Row(modifier = Modifier.padding(AppTheme.dimen.safeSpace,
        10.dp,
        AppTheme.dimen.safeSpace,
        0.dp)) {
        Text(text = "标签", fontSize = 12.sp, color = colorResource(id = R.color.color_4b5057),
            modifier = Modifier
                .border(1.dp, colorResource(id = R.color.color_p10282a2e), RoundedCornerShape(4.dp))
                .padding(10.dp, 5.dp)
                .clickable {
                    RouteUtils.navTo(navCtrl, RouteName.LABEL)
                })
        Box(Modifier.width(10.dp))
        Text(text = "标签", fontSize = 12.sp, color = colorResource(id = R.color.color_4b5057),
            modifier = Modifier
                .border(1.dp, colorResource(id = R.color.color_p10282a2e), RoundedCornerShape(4.dp))
                .padding(10.dp, 5.dp)
                .clickable {
                    RouteUtils.navTo(navCtrl, RouteName.LABEL)
                })
    }
}

@Composable
fun TopicItemFunction(topicData: TopicData) {
    Row(modifier = Modifier.height(62.dp)) {
        TextButton(onClick = { showToast("转发") },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()) {
            Image(painter = painterResource(id = R.drawable.forward), contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
            Text(text = "转发", fontSize = 13.sp, color = AppTheme.colors.textPrimary)
        }
        TextButton(onClick = { showToast("评论") },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()) {
            Image(painter = painterResource(id = R.drawable.comment), contentDescription = null,
                modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))
            Text(text = "评论", fontSize = 13.sp, color = AppTheme.colors.textPrimary)
        }

        var click by remember { mutableStateOf(0) }
        val transY: Float by animateFloatAsState(if ((click > 0) and topicData.isPraise) -200f else 0f,
            animationSpec = tween(durationMillis = if ((click > 0) and topicData.isPraise) 2000 else 1))
        Row(modifier = Modifier
            .clickable {
                click++
                EventBus
                    .getDefault()
                    .post(EventTopicPraise(topicData.id, !topicData.isPraise))
            }
            .weight(1f)
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Box {
                Image(painter = painterResource(id = if (topicData.isPraise) R.drawable.praise_pink else R.drawable.praise),
                    contentDescription = null,
                    modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp))

                if ((click > 0) and topicData.isPraise) {
                    for (i in 0..5) {
                        val transX = i % 4 * 5
                        val tempTransY = transY * (i % 5 * 0.2 + 1).toFloat()
                        if (tempTransY > -190f)
                            Image(
                                painter = painterResource(id = R.drawable.icon_like),
                                contentDescription = null,
                                modifier = Modifier
                                    .offset(transX.dp, 0.dp)
                                    .graphicsLayer(translationY = tempTransY)
                            )
                    }
                }
            }
            Text(text = "奶一口", fontSize = 13.sp, color = AppTheme.colors.textPrimary)
        }
    }
}