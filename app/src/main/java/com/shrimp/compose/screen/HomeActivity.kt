package com.shrimp.compose.screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.shrimp.compose.screen.start.StartPage
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.AppThemeLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppThemeLayout {
                // Update the system bars to be translucent
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = AppTheme.colors.background) {
                    StartPage(this)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override
            fun handleOnBackPressed() {
                val intent = Intent(Intent.ACTION_MAIN);
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        })
    }
}