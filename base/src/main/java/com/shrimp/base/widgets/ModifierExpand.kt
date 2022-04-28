package com.shrimp.base.widgets

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType.Companion.KeyUp
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

/**
 * Created by chasing on 2022/4/26.
 */
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.interceptKey(key: Key, onKeyEvent: () -> Unit): Modifier {
    return this.onPreviewKeyEvent {
        if (it.key == key && it.type == KeyUp) { // fire onKeyEvent on KeyUp to prevent duplicates
            onKeyEvent()
            true
        } else it.key == key // only pass the key event to children if it's not the chosen key
    }
}