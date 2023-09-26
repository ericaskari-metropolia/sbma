package com.sbma.linkup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sbma.linkup.R


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
