package com.sbma.linkup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sbma.linkup.presentation.screens.CameraScreen
import com.sbma.linkup.presentation.screens.UserConnectionsScreenProvider
import com.sbma.linkup.presentation.screens.UserProfileScreenProvider
import com.sbma.linkup.user.User


@Composable
fun NavigationGraph(
    navController: NavHostController,
    user: User,
) {
    NavHost(navController, startDestination = BottomNavItem.Profile.screen_route) {
        composable(BottomNavItem.QRCode.screen_route) {
            QRCodeScreen()
        }
        composable(BottomNavItem.MyContacts.screen_route) {
            UserConnectionsScreenProvider(user)
        }
        composable(BottomNavItem.Profile.screen_route) {
            UserProfileScreenProvider(user)
        }
        composable(BottomNavItem.Camera.screen_route) {
            CameraScreen()
        }
        composable(BottomNavItem.Setting.screen_route) {
            SettingScreen()
        }
    }
}
