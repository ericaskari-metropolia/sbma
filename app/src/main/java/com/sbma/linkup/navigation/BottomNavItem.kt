package com.sbma.linkup.navigation

import com.sbma.linkup.R

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object Share : BottomNavItem("Share", R.drawable.icon_share, "share")
    object MyContacts : BottomNavItem("My Contacts", R.drawable.card_icon, "my_contacts_route")
    object Profile : BottomNavItem("Profile", R.drawable.user_icon, "profile")
    object Receive : BottomNavItem("Receive", R.drawable.receive_icon, "receive")
    object Setting : BottomNavItem("Setting", R.drawable.setting_icon, "setting_route")
}