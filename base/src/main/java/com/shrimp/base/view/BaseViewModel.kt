package com.shrimp.base.view

import android.app.Application
import android.content.Intent
import androidx.lifecycle.*

/**
 * Created by chasing on 2021/10/20.
 * 传入Application是为了可以使用getString及操作数据库等功能
 * 不使用Activity/Fragment避免内存泄露
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {
    val dialogShow: MutableLiveData<Boolean> = MutableLiveData()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }

    open fun handleIntent(intent: Intent) {}
}