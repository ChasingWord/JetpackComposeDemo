package com.shrimp.compose.screen.main.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.load
import coil.request.ImageRequest
import com.shrimp.base.view.BaseActivity
import com.shrimp.compose.R
import com.shrimp.compose.bean.AppFunInfo
import com.shrimp.compose.bean.UserInfo
import com.shrimp.compose.engine.GlobalInfoManager
import com.shrimp.compose.screen.main.vm.VMHomeMine

/**
 * Created by chasing on 2022/3/22.
 */
@Composable
fun HomeMine(
    activity: BaseActivity,
    vmHomeMine: VMHomeMine = hiltViewModel(),
) {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    GlobalInfoManager.userInfo.removeObservers(activity)
    GlobalInfoManager.userInfo.observe(activity) {
        userInfo = it.copy()
    }

    val context = LocalContext.current
    LazyColumn(modifier = Modifier.background(colorResource(id = R.color.color_f5f5f5))) {
        item {
            HomeMineUserInfo(vmHomeMine, userInfo)
        }
        item {
            HomeMineDynamicInfo(userInfo)
        }
        item {
            Text(text = "资源管理",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.color_2c2c2c),
                modifier = Modifier.padding(13.dp, 10.dp, 13.dp, 17.dp))
        }
        item {
            HomeMineResourceFun()
        }
        item {
            HomeMineEarnMoneyInfo()
        }
        item {
            HomeMineAppFun()
        }
        item {
            Row(modifier = Modifier
                .padding(13.dp, 10.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(text = "官网：",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.color_aeaeae))
                Text(text = "https://www.baidu.com",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.color_0fa8eb),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "官网...", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }
}

@SuppressLint("StaticFieldLeak")
private lateinit var vipBgImageView: ImageView

@Composable
fun HomeMineUserInfo(vmHomeMine: VMHomeMine, userInfo: UserInfo) {
    val context = LocalContext.current
    Box {
        Image(painter = painterResource(id = R.mipmap.personal_top_bg),
            contentDescription = "顶部背景图",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1080 / 474f),
            contentScale = ContentScale.FillWidth)

        Row(Modifier
            .padding(20.dp, 0.dp, 20.dp, 0.dp)
            .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically) {
            Box {
                AsyncImage(modifier = Modifier
                    .clip(CircleShape)
                    .size(64.dp),
                    model = ImageRequest.Builder(context)
                        .data("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01bdd05a436090a80121974142c9f3.gif&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1634225053&t=eaa097d38a32f901b7096fae16bfb98f")
//                        .data("https://t7.baidu.com/it/u=1595072465,3644073269&fm=193&f=GIF")
                        .crossfade(true)
                        .build(),
                    contentDescription = null)
                Image(painter = painterResource(id = R.mipmap.v_icon), contentDescription = null,
                    Modifier.align(Alignment.BottomEnd))
            }
            Column(Modifier
                .padding(10.dp)
                .weight(1f)) {
                Text("昵称", fontSize = 14.sp, color = colorResource(id = R.color.color_2c2c2c))
                Text("称呼", fontSize = 13.sp, color = colorResource(id = R.color.color_aeaeae))
            }

            Surface(shape = RoundedCornerShape(5.dp), color = colorResource(R.color.color_ff609d)) {
                Text(text = if (userInfo.hadSign) "已签到" else "签到",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            vmHomeMine.refresh()
                            Toast
                                .makeText(context, "签到了", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .padding(10.dp, 4.dp))
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1082 / 212f)
            .align(Alignment.BottomCenter)) {
            AndroidView(factory = { context ->
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
                vipBgImageView = ImageView(context)
                vipBgImageView.layoutParams = lp
                vipBgImageView.apply {
                    load(R.mipmap.personal_vip_bg)
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight())

            Text(text = "开通VIP会员", fontSize = 17.sp, fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.color_fff0dd),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(37.dp, 8.dp))

            Row(modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset((-37).dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "教程免费学 | 了解更多",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.white),
                    modifier = Modifier.offset((-4).dp, 0.dp))
                Icon(painter = painterResource(id = R.mipmap.arrow_right_white_14x34),
                    contentDescription = "arrow", tint = colorResource(id = R.color.white))
            }
        }
    }
}

@Composable
fun HomeMineDynamicInfo(userInfo: UserInfo) {
    Row(verticalAlignment = Alignment.Bottom) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Text(text = userInfo.dynamicCount.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = R.color.color_2c2c2c))
            Text(text = "动态", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Text(text = userInfo.focusCount.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = R.color.color_2c2c2c))
            Text(text = "关注", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Text(text = userInfo.fansCount.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = colorResource(id = R.color.color_2c2c2c))
            Text(text = "粉丝", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Image(painter = painterResource(id = R.mipmap.personal_coupon),
                contentDescription = "卡券包",
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
            Text(text = "卡券包", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
    }
}

@Composable
fun HomeMineResourceFun() {
    Row {
        HomeMineResourceFunSingle(resId = R.mipmap.personal_my_publish, name = "我发布的", funType = 0)
        HomeMineResourceFunSingle(resId = R.mipmap.personal_my_bought, name = "我买到的", funType = 1)
        HomeMineResourceFunSingle(resId = R.mipmap.personal_my_sale, name = "我卖出的", funType = 2)
    }
}

@Composable
fun RowScope.HomeMineResourceFunSingle(resId: Int, name: String, funType: Int) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .weight(1f)
        .clickable {
            when (funType) {
                0 -> Toast
                    .makeText(context, name, Toast.LENGTH_SHORT)
                    .show()
                1 -> Toast
                    .makeText(context, name, Toast.LENGTH_SHORT)
                    .show()
                else -> Toast
                    .makeText(context, name, Toast.LENGTH_SHORT)
                    .show()
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = resId), contentDescription = null)
        Text(text = name, fontSize = 11.sp, color = colorResource(id = R.color.color_2c2c2c),
            modifier = Modifier.padding(0.dp, 7.dp, 0.dp, 0.dp))
    }
}

@Composable
fun HomeMineEarnMoneyInfo() {
    Card(shape = MaterialTheme.shapes.medium, elevation = 6.dp,
        backgroundColor = colorResource(id = R.color.white),
        modifier = Modifier
            .fillMaxWidth()
            .padding(13.dp)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.mipmap.personal_earn),
                    contentDescription = null
                )
                Text(text = "邀请赚钱",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.color_2c2c2c),
                    modifier = Modifier.padding(11.dp, 0.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "可提现",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.color_aeaeae),
                    modifier = Modifier.padding(8.dp, 0.dp))
                Image(
                    painter = painterResource(id = R.mipmap.arrow_right_gray_22x50),
                    contentDescription = null
                )
            }
            Row(modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
                .background(color = colorResource(id = R.color.color_10ff609d),
                    shape = RoundedCornerShape(4.dp))
                .padding(13.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.mipmap.personal_invitation_icon),
                    contentDescription = null)
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(9.dp, 0.dp, 0.dp, 0.dp)) {
                    Text(text = "我的邀请码：XXXXXX", color = colorResource(id = R.color.color_ff609d),
                        fontSize = 13.sp)
                    Text(text = "邀请好友，躺着也能赚钱", color = colorResource(id = R.color.color_ff609d),
                        fontSize = 11.sp)
                }

                val context = LocalContext.current
                TextButton(onClick = {
                    Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show()
                }, border = BorderStroke(1.dp, colorResource(id = R.color.color_ff609d)),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = colorResource(id = R.color.transparent),
                        contentColor = colorResource(id = R.color.transparent),
                        disabledContentColor = colorResource(id = R.color.transparent))) {
                    Text(text = "复制邀请码", color = colorResource(id = R.color.color_ff609d),
                        fontSize = 13.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeMineAppFun() {
    Card(shape = MaterialTheme.shapes.medium, elevation = 6.dp,
        backgroundColor = colorResource(id = R.color.white),
        modifier = Modifier
            .fillMaxWidth()
            .padding(13.dp, 0.dp, 13.dp, 10.dp)) {
        val list = mutableListOf<AppFunInfo>()
        list.add(AppFunInfo("活跃度积分", R.mipmap.personal_integral))
        list.add(AppFunInfo("活跃度等级", R.mipmap.personal_activity_level))
        list.add(AppFunInfo("学霸助手", R.mipmap.personal_helper))
        list.add(AppFunInfo("我的学币", R.mipmap.personal_coin, "2222"))
        list.add(AppFunInfo("浏览记录", R.mipmap.personal_browser_history))
        list.add(AppFunInfo("我的求职", R.mipmap.personal_job))
        list.add(AppFunInfo("我发布的项目", R.mipmap.personal_publish_project))
        list.add(AppFunInfo("投诉/售后", R.mipmap.personal_sale_service))
        list.add(AppFunInfo("我的简历", R.mipmap.personal_resume))
        list.add(AppFunInfo("我的收藏", R.mipmap.personal_collect))
        list.add(AppFunInfo("我的设置", R.mipmap.personal_setting))
        Column {
            for (index in 0 until list.size / 3)
                Row(modifier = Modifier.fillMaxWidth()) {
                    HomeMineAppFunSingle(list[index * 3])
                    HomeMineAppFunSingle(list[index * 3 + 1])
                    HomeMineAppFunSingle(list[index * 3 + 2])
                }
        }
    }
}

@Composable
fun RowScope.HomeMineAppFunSingle(appFunInfo: AppFunInfo) {
    Column(modifier = Modifier
        .padding(0.dp, 31.dp, 0.dp, 0.dp)
        .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = appFunInfo.resId),
            contentDescription = null)
        Text(text = appFunInfo.name, fontSize = 13.sp,
            color = colorResource(id = R.color.color_2c2c2c),
            modifier = Modifier.padding(0.dp, 9.dp, 0.dp, 0.dp))
        Text(text = appFunInfo.description, fontSize = 13.sp,
            color = colorResource(id = R.color.color_ff609d),
            modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 0.dp))
    }
}