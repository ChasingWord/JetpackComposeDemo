package com.shrimp.compose.screen.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.shrimp.base.view.BaseActivity
import com.shrimp.compose.screen.main.ui.Home
import com.shrimp.compose.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                // Update the system bars to be translucent
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = AppTheme.colors.background) {
                    Home(this)
                }
            }
        }
    }
}