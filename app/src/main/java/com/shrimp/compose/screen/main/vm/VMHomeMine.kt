package com.shrimp.compose.screen.main.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shrimp.base.view.BaseViewModel
import com.shrimp.compose.bean.UserInfo
import com.shrimp.compose.screen.main.dp.DPHomeMine

/**
 * Created by chasing on 2022/4/14.
 */
class VMHomeMine(application: Application) : BaseViewModel(application) {

    val userInfo = MutableLiveData(UserInfo())

    private val dpHomeMine: DPHomeMine = DPHomeMine()

    fun refresh() {
        val value = userInfo.value ?: UserInfo()
        value.dynamicCount = 100
        value.focusCount = 999
        value.fansCount = 99
        value.hadSign = true
        userInfo.value = value
    }
}