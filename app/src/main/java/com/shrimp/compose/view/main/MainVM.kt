package com.shrimp.compose.view.main

import androidx.lifecycle.ViewModelProvider
import com.shrimp.base.view.BaseActivity
import com.shrimp.compose.vm.VMHomeMine

/**
 * Created by chasing on 2022/4/15.
 */
class MainVM {
    lateinit var vmHomeMine: VMHomeMine

    fun initVM(activity: BaseActivity) {
        vmHomeMine = ViewModelProvider(activity).get(VMHomeMine::class.java)
        activity.addViewModel(vmHomeMine)
    }
}