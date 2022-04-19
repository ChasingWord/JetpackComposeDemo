package com.shrimp.compose.engine

import androidx.lifecycle.MutableLiveData
import com.shrimp.compose.bean.UserInfo

/**
 * Created by chasing on 2022/4/15.
 */
object GlobalInfoManager {
    val userInfo = MutableLiveData(UserInfo())
}