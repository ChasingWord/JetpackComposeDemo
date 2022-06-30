package com.shrimp.compose.screen.login.vm

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shrimp.base.utils.showToast
import com.shrimp.compose.bean.UserInfo
import com.shrimp.compose.engine.GlobalInfoManager
import javax.inject.Inject

/**
 * Created by chasing on 2022/6/22.
 */
class VMLogin @Inject constructor() : ViewModel() {
    var hadLogin = MutableLiveData(false)

    fun login(phone: String, checkCodeOrPwd: String, isLoginByCheckCode: Boolean) {
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号")
        } else if (TextUtils.isEmpty(checkCodeOrPwd)) {
            showToast(if (isLoginByCheckCode) "请输入验证码" else "请输入密码")
        } else {
            hadLogin.value = true
            GlobalInfoManager.userInfo.value = UserInfo(hadLogin = true)
            showToast("登录成功")
        }
    }
}