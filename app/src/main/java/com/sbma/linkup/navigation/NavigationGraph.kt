package com.sbma.linkup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sbma.linkup.presentation.screens.CameraScreen
import com.sbma.linkup.presentation.screens.MainShareScreen
import com.sbma.linkup.presentation.screens.QRCodeScreen
import com.sbma.linkup.presentation.screens.SettingScreen
import com.sbma.linkup.presentation.screens.UserConnectionsScreenProvider
import com.sbma.linkup.presentation.screens.UserProfileScreenProvider
import com.sbma.linkup.presentation.screens.UserShareScreenProvider
import com.sbma.linkup.presentation.screens.bluetooth.ShareViaBluetoothScreenProvider
import com.sbma.linkup.presentation.screens.nfc.NfcScanScreen
import com.sbma.linkup.user.User


@Composable
fun NavigationGraph(
    navController: NavHostController,
    user: User,
) {

    NavHost(navController, startDestination = "profile") {
        /**
         * tab of the bottom navigation bar
         */
        composable(BottomNavItem.QRCode.screen_route) {
            QRCodeScreen()
        }
        /**
         * tab of the bottom navigation bar
         */
        composable(BottomNavItem.MyContacts.screen_route) {
            UserConnectionsScreenProvider(user)
        }
        /**
         * tab of the bottom navigation bar
         */
        composable(BottomNavItem.Camera.screen_route) {
            CameraScreen()
        }
        /**
         * tab of the bottom navigation bar
         */
        composable(BottomNavItem.Setting.screen_route) {
            SettingScreen()
        }
        /**
         * tab of the bottom navigation bar
         * Navigates to {UserShareScreenProvider}  when user clicks on Share button.
         */
        composable("profile") {
            UserProfileScreenProvider(
                user,
                onShareClick = { navController.navigate("profile/share") },
                onEditClick = {},
            )
        }
        /**
         *  Choose what cards to share and save them to datastore so we wouldn't need to transfer it for each navigation.
         *  After user presses submit button it will navigate to .../share/choosemethod route to choose the mathod user wants to share.
         */
        composable("profile/share") {
            UserShareScreenProvider(user) {
                navController.navigate("profile/share/choosemethod")
            }
        }
        /**
         * Simple page with three buttons and three callbacks which we can navigate to the wanted route.
         */
        composable("profile/share/choosemethod") {
            MainShareScreen(
                onBluetoothClick = {
                    navController.navigate("profile/share/bluetooth")
                },
                onNfcClick = {
                    navController.navigate("profile/share/nfc")
                },
                onQrCodeClick = {
                    navController.navigate("profile/share/qr")
                }
            )
        }
        /**
         * Bluetooth method of sharing user profile.
         * at this point json string should be already saved to datastore and available.
         */
        composable("profile/share/bluetooth") {
            ShareViaBluetoothScreenProvider()
        }

        composable("profile/share/nfc") {
            NfcScanScreen()
        }
    }
}
