package com.sbma.linkup.navigation

import com.sbma.linkup.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object QRCode : BottomNavItem("My QR", R.drawable.qr_icon,"qr_code_route")
    object MyCards: BottomNavItem("Cards",R.drawable.card_icon,"my_cards_route")
    object Profile: BottomNavItem("Profile",R.drawable.profile_icon,"profile_route")
    object Camera: BottomNavItem("Camera",R.drawable.camera_icon,"camera_route")
    object Setting: BottomNavItem("Setting",R.drawable.settings_icon,"setting_route")
}