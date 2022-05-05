package com.shrimp.compose.common

/**
 * Created by chasing on 2022/4/19.
 * 所有界面的路由名称
 * 注意：
 * 同一个界面内的多个路由，不需要界面跳转动画的情况下，路由名称需要包含同一个前缀
 * 例如HOME和COMMUNITY都有前缀"root"，以"_"进行分隔
 */
object RouteName {
    const val NAV_ROOT = "root"

    const val HOME = "root_home"
    const val COMMUNITY = "root_community"
    const val MESSAGE = "root_message"
    const val MINE = "root_mine"

    const val LABEL = "label"
    const val COMMUNITY_PERSONAL = "communityPersonal"
    const val WEB_VIEW = "webView"
}