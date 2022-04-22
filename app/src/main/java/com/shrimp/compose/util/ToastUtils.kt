package com.shrimp.compose.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import com.shrimp.compose.MyApplication

private var mToast: Toast? = null

/**
 * 显示时间较短的吐司
 *
 * @param text String，显示的内容
 */
fun showToast(text: String?, context: Context = MyApplication.CONTEXT) {
    if (TextUtils.isEmpty(text)) return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(text, Toast.LENGTH_SHORT, context)
    } else {
        Handler(context.mainLooper).post { showToast(text, Toast.LENGTH_SHORT, context) }
    }
}

/**
 * 显示时间较短的吐司
 *
 * @param resId int，显示内容的字符串索引
 */
fun showToast(resId: Int, context: Context = MyApplication.CONTEXT) {
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(resId, Toast.LENGTH_SHORT, context)
    } else {
        Handler(context.mainLooper).post { showToast(resId, Toast.LENGTH_SHORT, context) }
    }
}

/**
 * 显示时间较长的吐司
 *
 * @param text String，显示的内容
 */
fun showLongToast(text: String?, context: Context? = MyApplication.CONTEXT) {
    if (context == null || TextUtils.isEmpty(text)) return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(text, Toast.LENGTH_LONG, context)
    } else {
        Handler(context.mainLooper).post { showToast(text, Toast.LENGTH_LONG, context) }
    }
}

/**
 * 显示时间较长的吐司
 *
 * @param resId int，显示内容的字符串索引
 */
fun showLongToast(resId: Int, context: Context? = MyApplication.CONTEXT) {
    if (context == null) return
    if (Thread.currentThread() === Looper.getMainLooper().thread) {
        showToast(resId, Toast.LENGTH_LONG, context)
    } else {
        Handler(context.mainLooper).post { showToast(resId, Toast.LENGTH_LONG, context) }
    }
}

private fun showToast(text: String?, duration: Int, context: Context? = MyApplication.CONTEXT) {
    if (TextUtils.isEmpty(text)) return
    cancelToast()
    if (mToast == null) {
        mToast = Toast.makeText(context, text, duration)
    } else {
        mToast?.setText(text)
        mToast?.duration = duration
    }
    mToast?.show()
}

fun showToast(res: Int, duration: Int, context: Context? = MyApplication.CONTEXT) {
    cancelToast()
    if (mToast == null) {
        mToast = Toast.makeText(context, res, duration)
    } else {
        mToast?.setText(res)
        mToast?.duration = duration
    }
    mToast?.show()
}

fun cancelToast() {
    mToast?.cancel()
}