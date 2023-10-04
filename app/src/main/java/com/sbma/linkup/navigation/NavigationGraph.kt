package com.sbma.linkup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sbma.linkup.EditProfile.EditProfileScreen
import com.sbma.linkup.presentation.screens.CameraScreen
import com.sbma.linkup.presentation.screens.MainShareScreen
import com.sbma.linkup.presentation.screens.SettingScreen
import com.sbma.linkup.presentation.screens.UserConnectionsScreenProvider
import com.sbma.linkup.presentation.screens.UserProfileScreenProvider
import com.sbma.linkup.presentation.screens.UserShareScreenProvider
import com.sbma.linkup.presentation.screens.bluetooth.ShareViaBluetoothScreenProvider
import com.sbma.linkup.presentation.screens.nfc.NfcReceiveScreen
import com.sbma.linkup.presentation.screens.nfc.NfcScanScreen
import com.sbma.linkup.user.User
import com.sbma.linkup.util.MyQrCode


@Composable
fun NavigationGraph(
    navController: NavHostController,
    user: User,
) {

    NavHost(navController, startDestination = "profile") {
        /**
         * tab of the bottom navigation bar
         */
        composable(BottomNavItem.Share.screen_route) {
            UserShareScreenProvider(user) {
                navController.navigate("share/choosemethod")
            }
        }
        /**
         * Simple page with three buttons and three callbacks which we can navigate to the wanted route.
         */
        composable("share/choosemethod") {
            MainShareScreen(
                onBluetoothClick = {
                    navController.navigate("share/bluetooth")
                },
                onNfcClick = {
                    navController.navigate("share/nfc")
                },
                onQrCodeClick = {
                    navController.navigate("share/qr")
                },
                isReceiving = false,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        /**
         * Bluetooth method of sharing user profile.
         * at this point json string should be already saved to datastore and available.
         */
        composable("share/bluetooth") {
            ShareViaBluetoothScreenProvider()
        }

        composable("share/nfc") {
            NfcScanScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("share/qr") {
            MyQrCode()
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
        composable(BottomNavItem.Receive.screen_route) {
            MainShareScreen(
                onBluetoothClick = {
                    navController.navigate("receive/bluetooth")
                },
                onNfcClick = {
                    navController.navigate("receive/nfc")
                },
                onQrCodeClick = {
                    navController.navigate("receive/qr")
                },
                isReceiving = true,
                onBackClick = {
                    navController.popBackStack()
                }

            )
        }
        /**
         * Bluetooth method of sharing user profile.
         * at this point json string should be already saved to datastore and available.
         */
        composable("receive/bluetooth") {
        }
        composable("receive/nfc") {
            NfcReceiveScreen(
                onBackClick = {
                    navController.popBackStack()
                })
        }
        composable("receive/qr") {
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
                onEditClick = { navController.navigate("profile/edit") },
            )
        }
        /**
         *  Edit profile screen
         *  After user presses save button it will navigate back to profile route.
         */
        composable("profile/edit") {
            EditProfileScreen(
                user,
                onSave = {
                    navController.navigate("profile")
                })
        }
    }
}
