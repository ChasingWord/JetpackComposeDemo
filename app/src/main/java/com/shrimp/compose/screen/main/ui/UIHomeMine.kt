package com.shrimp.compose.screen.main.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavHostController
import coil.load
import com.shrimp.base.utils.RouteUtils
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.base.utils.showToast
import com.shrimp.base.widgets.ImageNet
import com.shrimp.compose.R
import com.shrimp.compose.bean.AppFunInfo
import com.shrimp.compose.bean.UserInfo
import com.shrimp.compose.common.RouteName
import com.shrimp.compose.engine.GlobalInfoManager
import com.shrimp.compose.screen.main.vm.VMHomeMine
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_transparent

/**
 * Created by chasing on 2022/3/22.
 */
@Composable
fun HomeMine(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    activity: ComponentActivity,
    vmHomeMine: VMHomeMine = hiltViewModel(),
) {
    SystemStatusBarTransparent(isShowDarkIcon = true)
    var userInfo by remember { mutableStateOf(UserInfo()) }
    GlobalInfoManager.userInfo.removeObservers(activity)
    GlobalInfoManager.userInfo.observe(activity) {
        userInfo = it.copy()
    }

    LazyColumn(modifier = Modifier.background(color = AppTheme.colors.secondary)) {
        item {
            HomeMineUserInfo(vmHomeMine, userInfo)
        }
        item {
            HomeMineDynamicInfo(userInfo)
        }
        item {
            Text(text = "????????????",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(AppTheme.dimen.safeSpace,
                    10.dp,
                    AppTheme.dimen.safeSpace,
                    17.dp))
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
                .padding(AppTheme.dimen.safeSpace, 10.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(text = "?????????",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.color_aeaeae))
                Text(text = "https://www.baidu.com",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.color_0fa8eb),
                    modifier = Modifier.clickable {
                        RouteUtils.navTo(navCtrl, RouteName.WEB_VIEW, "https://www.baidu.com")
                    })
            }
        }
    }
}

@SuppressLint("StaticFieldLeak")
private lateinit var vipBgImageView: ImageView

@Composable
fun HomeMineUserInfo(vmHomeMine: VMHomeMine, userInfo: UserInfo) {
    Box {
        Image(painter = painterResource(id = R.drawable.personal_top_bg),
            contentDescription = "???????????????",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1080 / 474f),
            contentScale = ContentScale.FillWidth)

        Row(Modifier
            .padding(20.dp, 0.dp, 20.dp, 0.dp)
            .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically) {
            Box {
                ImageNet(url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01bdd05a436090a80121974142c9f3.gif&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1634225053&t=eaa097d38a32f901b7096fae16bfb98f",
                    shape = CircleShape,
                    size = 64.dp)
                Image(painter = painterResource(id = R.drawable.v_icon), contentDescription = null,
                    Modifier.align(Alignment.BottomEnd))
            }
            Column(Modifier
                .padding(10.dp)
                .weight(1f)) {
                Text("??????", fontSize = 14.sp, color = AppTheme.colors.textPrimary)
                Text("??????", fontSize = 13.sp, color = colorResource(id = R.color.color_aeaeae))
            }

            Surface(shape = RoundedCornerShape(5.dp), color = colorResource(R.color.color_ff609d)) {
                Text(text = if (userInfo.hadSign) "?????????" else "??????",
                    fontSize = 13.sp,
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            vmHomeMine.refresh()
                            showToast("?????????")
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

            Text(text = "??????VIP??????", fontSize = 17.sp, fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.color_fff0dd),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(37.dp, 8.dp))

            Row(modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset((-37).dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "??????????????? | ????????????",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.white),
                    modifier = Modifier.offset((-4).dp, 0.dp))
                Icon(painter = painterResource(id = R.drawable.arrow_right_white_14x34),
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
                color = AppTheme.colors.textPrimary)
            Text(text = "??????", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Text(text = userInfo.focusCount.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = AppTheme.colors.textPrimary)
            Text(text = "??????", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Text(text = userInfo.fansCount.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = AppTheme.colors.textPrimary)
            Text(text = "??????", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 10.dp)) {
            Image(painter = painterResource(id = R.drawable.personal_coupon),
                contentDescription = "?????????",
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
            Text(text = "?????????", fontSize = 11.sp, color = colorResource(id = R.color.color_aeaeae))
        }
    }
}

@Composable
fun HomeMineResourceFun() {
    Row {
        HomeMineResourceFunSingle(resId = R.drawable.personal_my_publish, name = "????????????") {
            showToast("????????????")
        }
        HomeMineResourceFunSingle(resId = R.drawable.personal_my_bought, name = "????????????") {
            showToast("????????????")
        }
        HomeMineResourceFunSingle(resId = R.drawable.personal_my_sale, name = "????????????") {
            showToast("????????????")
        }
    }
}

@Composable
fun RowScope.HomeMineResourceFunSingle(resId: Int, name: String, clickListener: () -> Unit) {
    Column(modifier = Modifier
        .weight(1f)
        .clickable {
            clickListener.invoke()
        },
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = resId), contentDescription = null)
        Text(text = name, fontSize = 11.sp, color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(0.dp, 7.dp, 0.dp, 0.dp))
    }
}

@Composable
fun HomeMineEarnMoneyInfo() {
    Card(shape = MaterialTheme.shapes.medium, elevation = 6.dp,
        backgroundColor = colorResource(id = R.color.white),
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimen.safeSpace)) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.personal_earn),
                    contentDescription = null
                )
                Text(text = "????????????",
                    fontSize = 13.sp,
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.padding(11.dp, 0.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "?????????",
                    fontSize = 13.sp,
                    color = colorResource(id = R.color.color_aeaeae),
                    modifier = Modifier.padding(8.dp, 0.dp))
                Image(
                    painter = painterResource(id = R.drawable.arrow_right_gray_22x50),
                    contentDescription = null
                )
            }
            Row(modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
                .background(color = colorResource(id = R.color.color_10ff609d),
                    shape = RoundedCornerShape(4.dp))
                .padding(AppTheme.dimen.safeSpace, 10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.personal_invitation_icon),
                    contentDescription = null)
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(9.dp, 0.dp, 0.dp, 0.dp)) {
                    Text(text = "??????????????????XXXXXX", color = colorResource(id = R.color.color_ff609d),
                        fontSize = 13.sp)
                    Text(text = "?????????????????????????????????", color = colorResource(id = R.color.color_ff609d),
                        fontSize = 11.sp)
                }

                val context = LocalContext.current
                TextButton(onClick = {
                    showToast("????????????")
                }, border = BorderStroke(1.dp, colorResource(id = R.color.color_ff609d)),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = color_transparent,
                        contentColor = color_transparent,
                        disabledContentColor = color_transparent)) {
                    Text(text = "???????????????", color = colorResource(id = R.color.color_ff609d),
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
            .padding(AppTheme.dimen.safeSpace, 0.dp, AppTheme.dimen.safeSpace, 10.dp)) {
        val list = mutableListOf<AppFunInfo>()
        list.add(AppFunInfo("???????????????", R.drawable.personal_integral))
        list.add(AppFunInfo("???????????????", R.drawable.personal_activity_level))
        list.add(AppFunInfo("????????????", R.drawable.personal_helper))
        list.add(AppFunInfo("????????????", R.drawable.personal_coin, "2222"))
        list.add(AppFunInfo("????????????", R.drawable.personal_browser_history))
        list.add(AppFunInfo("????????????", R.drawable.personal_job))
        list.add(AppFunInfo("??????????????????", R.drawable.personal_publish_project))
        list.add(AppFunInfo("??????/??????", R.drawable.personal_sale_service))
        list.add(AppFunInfo("????????????", R.drawable.personal_resume))
        list.add(AppFunInfo("????????????", R.drawable.personal_collect))
        list.add(AppFunInfo("????????????", R.drawable.personal_setting))
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
            color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(0.dp, 9.dp, 0.dp, 0.dp))
        Text(text = appFunInfo.description, fontSize = 13.sp,
            color = colorResource(id = R.color.color_ff609d),
            modifier = Modifier.padding(0.dp, 3.dp, 0.dp, 0.dp))
    }
}