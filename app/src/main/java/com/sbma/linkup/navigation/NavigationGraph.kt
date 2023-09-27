package com.sbma.linkup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sbma.linkup.presentation.screens.CameraScreen


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Profile.screen_route) {
        composable(BottomNavItem.QRCode.screen_route) {
            QRCodeScreen()
        }
        composable(BottomNavItem.MyCards.screen_route) {
            MyCardsScreen()
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfileScreen()
        }
        composable(BottomNavItem.Camera.screen_route) {
            CameraScreen()
        }
        composable(BottomNavItem.Setting.screen_route) {
            SettingScreen()
        }
    }
}
