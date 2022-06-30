package com.shrimp.compose.screen.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shrimp.base.utils.RouteUtils
import com.shrimp.base.utils.RouteUtils.back
import com.shrimp.base.utils.SystemStatusBarTransparent
import com.shrimp.compose.R
import com.shrimp.compose.common.RouteName
import com.shrimp.compose.screen.login.vm.VMLogin
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_f5f5f5
import com.shrimp.compose.ui.theme.color_ffffff
import com.shrimp.compose.ui.widgets.CustomEditView
import com.shrimp.compose.ui.widgets.PASSWORD_VISIBLE_ICON_PADDING

/**
 * Created by chasing on 2022/6/22.
 */
@Composable
fun Login(
    navCtrl: NavHostController,
    scaffoldState: ScaffoldState,
    vmLogin: VMLogin = hiltViewModel(),
) {
    SystemStatusBarTransparent(true)

    val hadLogin = vmLogin.hadLogin.observeAsState()
    if (hadLogin.value == true) {
        vmLogin.hadLogin.value = false
        RouteUtils.navTo(navCtrl,
            RouteName.HOME,
            backStackRouteName = RouteName.HOME,
            isInclusive = true,
            isSaveState = false)
    }

    Column(modifier = Modifier
        .background(color = AppTheme.colors.primary)
        .fillMaxWidth()
        .fillMaxHeight()) {

        var isLoginByCheckCode by remember { mutableStateOf(true) }

        Row(modifier = Modifier
            .statusBarsPadding()
            .padding(AppTheme.dimen.safeSpace, 0.dp)
            .height(AppTheme.dimen.toolbarHeight),
            verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier
                .clickable { navCtrl.back() }
                .padding(10.dp),
                painter = painterResource(id = R.drawable.arrow_left_60),
                contentDescription = "")
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {
                isLoginByCheckCode = !isLoginByCheckCode
            }) {
                Text(text = "密码登录", fontSize = 13.sp, color = AppTheme.colors.textPrimary)
                Image(painter = painterResource(id = R.drawable.arrow_right_36),
                    contentDescription = "")
            }
        }

        Text(text = if (isLoginByCheckCode) "欢迎登录绘学霸" else "输入您的账号",
            fontSize = 27.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.textPrimary,
            modifier = Modifier.padding(29.dp, 55.dp, 0.dp, 0.dp))
        Text(text = if (isLoginByCheckCode) "未注册将自动创建新账号" else "使用已注册手机号登录",
            fontSize = 13.sp, color = AppTheme.colors.textSecondary,
            modifier = Modifier.padding(29.dp, 4.dp, 0.dp, 0.dp))

        var inputPhone by remember { mutableStateOf("") }
        var inputCheckCodeOrPwd by remember { mutableStateOf("") }
        CustomEditView(
            modifier = Modifier
                .padding(29.dp, 20.dp, 29.dp, 0.dp),
            text = inputPhone,
            onValueChanged = {
                inputPhone = it
            },
            onDeleteClick = {
                inputPhone = ""
            },
            hintText = "请输入手机号",
            contentPaddingValues = PaddingValues(10.dp, 10.dp, 40.dp, 10.dp),
            deleteIcon = R.drawable.delete_gray_60,
            deleteIconPaddingEnd = 10.dp,
            showIndicatorLine = true,
            isHideKeyboard = false,
        )
        CustomEditView(
            modifier = Modifier
                .padding(29.dp, 20.dp, 29.dp, 0.dp)
                .background(color_f5f5f5, RoundedCornerShape(5.dp)),
            text = inputCheckCodeOrPwd,
            onValueChanged = {
                inputCheckCodeOrPwd = it
            },
            onDeleteClick = {
                inputCheckCodeOrPwd = ""
            },
            hintText = if (isLoginByCheckCode) "请输入验证码" else "请输入密码",
            isPassword = !isLoginByCheckCode,
            contentPaddingValues = PaddingValues(10.dp, 10.dp,
                if (isLoginByCheckCode) 40.dp else (40.dp + PASSWORD_VISIBLE_ICON_PADDING), 10.dp),
            deleteIcon = R.drawable.delete_gray_60,
            deleteIconPaddingEnd = 10.dp,
            showIndicatorLine = false,
            isHideKeyboard = true,
        )
        TextButton(modifier = Modifier
            .padding(29.dp, 29.dp, 29.dp, 0.dp)
            .fillMaxWidth()
            .background(AppTheme.colors.confirm, RoundedCornerShape(5.dp))
            .align(Alignment.CenterHorizontally),
            onClick = {
                vmLogin.login(inputPhone, inputCheckCodeOrPwd, isLoginByCheckCode)
            }) {
            Text(text = "登录", fontSize = 17.sp, color = color_ffffff,
                modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}