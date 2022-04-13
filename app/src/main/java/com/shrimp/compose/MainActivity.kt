package com.shrimp.compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.shrimp.compose.ui.theme.JepackComposeDemoTheme
import com.shrimp.compose.ui.view.Home

class MainActivity : ComponentActivity() {

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JepackComposeDemoTheme {
                // Update the system bars to be translucent
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    Home()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CheckPreview() {
    Home()
}
