package com.shrimp.base.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.shrimp.base.utils.ActivityUtil
import com.shrimp.base.utils.FixMemLeakUtil
import com.shrimp.base.widgets.dialog.ProgressDialog

/**
 * Created by chasing on 2021/10/19.
 */
abstract class BaseActivity : FragmentActivity() {
    protected lateinit var context: Activity
    protected var isPause = false

    private lateinit var dialog: ProgressDialog
    private var showLoadingTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        //统一判断，避免连续打开两个界面
        fun start(context: Context, clazz: Class<*>?) {
            start(context, Intent(context, clazz))
        }

        fun start(context: Context, intent: Intent) {
            if (intent.component != null && ActivityUtil.oneClickUtil.check(intent.component?.className)) return
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        context = this

        dialog = ProgressDialog()
        dialog.isCancelable = true
    }

    // 针对单Aty多页面的情况，一个页面对应一个ViewModel维护独立的数据
    fun addViewModel(vm: BaseViewModel) {
        vm.dialogShow.observe(this) { isShow ->
            if (isShow)
                showLoading()
            else
                hideLoading()
        }
        vm.handleIntent(intent)
        lifecycle.addObserver(vm)
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        isPause = true;
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        FixMemLeakUtil.fixLeak(this)
        dialog.onDestroy()
        super.onDestroy()
    }

    private fun showLoading() {
        if (isFinishing) return
        if (dialog.isShowing) return
        val fg: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fg.beginTransaction()
        dialog.show(ft, javaClass.name)
        showLoadingTime = System.currentTimeMillis()
    }

    /**
     * 如果showLoading的时间与hideLoading的时间相差太接近可能导致在调用dismiss的时候dialog还没有正真显示出来
     * 而dismiss之后dialog才正真显示出来，所以进行时间差判断（如果相差0.3s内就调用hide则延迟处理）
     */
    private fun hideLoading() {
        if (!isFinishing && dialog.isShowing) {
            val time = System.currentTimeMillis()
            if (time - showLoadingTime < 300) {
                handler.postDelayed({
                    if (!isFinishing) {
                        dialog.dismiss()
                    }
                }, 300 - (time - showLoadingTime))
            } else {
                dialog.dismiss()
            }
        }
    }

    fun startActivityForResult(clazz: Class<*>) {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) {
            val data = it.data
            val resultCode = it.resultCode
        }.launch(Intent(context, clazz))
    }

    fun registerPermissionsResult() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (entry in it) {
                if (!entry.value) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, entry.key)) {
                        // 被永久拒绝--需要显示请求理由
                        var toastString: String
                        when (entry.key) {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                toastString = "请前往手机系统设置界面进行设置存储权限！"
                            }
                            Manifest.permission.CAMERA -> {
                                toastString = "请前往手机系统设置界面进行设置拍照权限！"
                            }
                            Manifest.permission.CALL_PHONE -> {
                                toastString = "请前往手机系统设置界面进行设置拨打电话权限！"
                            }
                            Manifest.permission.WRITE_CALENDAR -> {
                                toastString = "请前往手机系统设置界面进行设置读写日历权限！"
                            }
                            else -> {
                                toastString = "请前往手机系统设置界面进行设置相应权限！"
                            }
                        }
                        ActivityUtil.showToast(this, toastString)
                    }
                }
            }
        }
    }
}