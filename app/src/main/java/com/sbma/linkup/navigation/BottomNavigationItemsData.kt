package com.sbma.linkup.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import com.sbma.linkup.R

@Stable
sealed class BottomNavigationItemsData(
    var title: String,
    @DrawableRes val icon: Int,
    var isSelected: Boolean,
//    @StringRes val description: Int,
    var screen_route: String,

    val animationType: ColorButtonAnimation = BellColorButton(
        tween(500),
        background = ButtonBackground(R.drawable.plus)
    ),
) {
    object Share : BottomNavigationItemsData(
        title = "Share",
        icon = R.drawable.card_send,
        isSelected = false,
//        description = R.string.Home,
        screen_route = "share",
    )

    object MyContacts : BottomNavigationItemsData(
        title = "My Network",
        icon = R.drawable.my_network,
        isSelected = false,
//        description = R.string.Bell
        screen_route = "connections",
    )

    object Profile : BottomNavigationItemsData(
        title = "Profile",
        icon = R.drawable.person,
        isSelected = false,
//        description = R.string.Message
        screen_route = "profile",
    )

    object Receive : BottomNavigationItemsData(
        title = "Receive",
        icon = R.drawable.card_receive,
        isSelected = false,
//        description = R.string.Heart
        screen_route = "receive",
    )

    object Settings : BottomNavigationItemsData(
        title = "Settings",
        icon = R.drawable.settings,
        isSelected = false,
//        description = R.string.Person
        screen_route = "setting_route",
    )
}


//val dropletButtons = listOf(
//    BottomNavigationItemsData(
//        title = "Share",
//        icon = R.drawable.home,
//        isSelected = false,
////        description = R.string.Home,
//        screen_route = "share",
//    ),
//    BottomNavigationItemsData(
//        title = "My Network",
//        icon = R.drawable.bell,
//        isSelected = false,
////        description = R.string.Bell
//        screen_route = "connections",
//    ),
//    BottomNavigationItemsData(
//        title = "Profile",
//        icon = R.drawable.message_buble,
//        isSelected = false,
////        description = R.string.Message
//        screen_route = "profile",
//    ),
//    BottomNavigationItemsData(
//        title = "Receive",
//        icon = R.drawable.heart,
//        isSelected = false,
////        description = R.string.Heart
//        screen_route = "receive",
//    ),
//    BottomNavigationItemsData(
//        title = "Settings",
//        icon = R.drawable.person,
//        isSelected = false,
////        description = R.string.Person
//        screen_route = "setting_route",
//    ),
//)

