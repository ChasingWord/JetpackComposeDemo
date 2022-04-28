package com.shrimp.network.entity.base

import android.text.TextUtils

/**
 * Created by chasing on 2021/10/21.
 */
data class ResponseResult<T>(
    var code: String = "",
    var data: T? = null,
) {
    companion object {
        fun getErrorMsg(msg: String) = when {
            TextUtils.isEmpty(msg) -> "当前网络不太稳定"
            msg.contains("Failed to connect to") -> "当前网络连接失败，请尝试在设置中检查APP联网控制权限是否开启！"
            msg.contains("connect timed out") or msg.contains("timeout") -> "当前网络不稳定，请求超时"
            msg.contains("HTTP 404") -> "链接异常"
            msg.contains("HTTP 500 Internal Server Error") or msg.contains("HTTP 502 Bad Gateway") -> "程序通信异常"
            msg.contains("No such file or directory") -> "未找到相应文件"
            msg.contains("No address associated with hostname") -> "请检测网络配置"
            else -> "当前网络不太稳定"
        }
    }

    fun isSuccess() = code == "00"

    fun getErrorMsg() = when (code) {
        "94" -> "未知异常"
        "95" -> "没有指定权限"
        "96" -> "请求数据异常"
        "97" -> "身份验证失败，请重新登录"
        "98" -> "网络不太稳定"
        "99" -> "服务器异常"
        else -> "网络异常"
    }
}