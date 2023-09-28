package com.sbma.linkup.navigation

import com.sbma.linkup.R

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {

    object QRCode : BottomNavItem("My QR", R.drawable.qr_icon, "qr_code_route")
    object MyContacts : BottomNavItem("My Contacts", R.drawable.card_icon, "my_contacts_route")
    object Profile : BottomNavItem("Profile", R.drawable.profile_icon, "profile")
    object Camera : BottomNavItem("Camera", R.drawable.camera_icon, "camera_route")
    object Setting : BottomNavItem("Setting", R.drawable.settings_icon, "setting_route")
}