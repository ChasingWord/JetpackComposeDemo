package com.shrimp.compose.screen.main

import androidx.lifecycle.ViewModelProvider
import com.shrimp.base.view.BaseActivity
import com.shrimp.compose.screen.main.vm.VMHomeMine

/**
 * Created by chasing on 2022/4/15.
 */
class MainVMManager {
    lateinit var vmHomeMine: VMHomeMine

    fun initVM(activity: BaseActivity) {
        vmHomeMine = ViewModelProvider(activity).get(VMHomeMine::class.java)
        activity.addViewModel(vmHomeMine)
    }
}