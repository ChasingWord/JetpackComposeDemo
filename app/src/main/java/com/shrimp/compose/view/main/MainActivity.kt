package com.shrimp.compose.view.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.shrimp.base.view.BaseActivity
import com.shrimp.compose.ui.theme.JepackComposeDemoTheme
import com.shrimp.compose.ui.view.Home

class MainActivity : BaseActivity() {

    private lateinit var mainVM: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        mainVM = MainVM()
        mainVM.initVM(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JepackComposeDemoTheme {
                // Update the system bars to be translucent
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Home(mainVM, this)
                }
            }
        }
    }
}