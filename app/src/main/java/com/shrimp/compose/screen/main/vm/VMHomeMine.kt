package com.shrimp.compose.screen.main.vm

import androidx.lifecycle.ViewModel
import com.shrimp.compose.bean.UserInfo
import com.shrimp.compose.engine.GlobalInfoManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by chasing on 2022/4/14.
 */
@HiltViewModel
class VMHomeMine @Inject constructor() : ViewModel() {

    fun refresh() {
        val value = GlobalInfoManager.userInfo.value ?: UserInfo()
        value.dynamicCount = 100
        value.focusCount = 999
        value.fansCount = 99
        value.hadSign = true
        GlobalInfoManager.userInfo.value = value
    }
}