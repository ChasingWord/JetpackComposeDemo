package com.shrimp.compose.engine

/**
 * Created by chasing on 2022/4/20.
 */
sealed class ViewAction {
    object FetchData : ViewAction()
    object Refresh : ViewAction()
}