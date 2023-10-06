package com.sbma.linkup.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sbma.linkup.presentation.screens.CameraScreen
import com.sbma.linkup.presentation.screens.ConnectionUserProfileScreenProvider
import com.sbma.linkup.presentation.screens.EditProfileScreen
import com.sbma.linkup.presentation.screens.MainShareScreen
import com.sbma.linkup.presentation.screens.ScanResultScreen
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
    paddingValues: PaddingValues,
    user: User,
) {

    NavHost(
        navController,
        modifier = Modifier.padding(paddingValues),
        startDestination = "profile"
    ) {
        /**
         * tab of the bottom navigation bar
         */
        composable("share") {
            UserShareScreenProvider(user) {shareId ->
                navController.navigate("share/${shareId}")
            }
        }
        /**
         * Simple page with three buttons and three callbacks which we can navigate to the wanted route.
         */
        composable(
            "share/{shareId}",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType }),

        ) {backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            MainShareScreen(
                onBluetoothClick = {
                    navController.navigate("share/${shareId}/bluetooth")
                },
                onNfcClick = {
                    navController.navigate("share/${shareId}/nfc")
                },
                onQrCodeClick = {
                    navController.navigate("share/${shareId}/qr")
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
        composable(
            "share/{shareId}/bluetooth",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType })
        ) {backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            shareId?.let {
                ShareViaBluetoothScreenProvider(it)
            }
        }

        composable(
            "share/{shareId}/nfc",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType })
        ) {backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            shareId?.let {
                NfcScanScreen(
                    shareId = it,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(
            "share/{shareId}/qr",
            arguments = listOf(navArgument("shareId") { type = NavType.StringType })
        ) {backStackEntry ->
            val shareId = backStackEntry.arguments?.getString("shareId")
            shareId?.let {
                MyQrCode(
                    shareId = it,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

        }
        /**
         * tab of the bottom navigation bar
         */
        composable("connections") {
            UserConnectionsScreenProvider(user) { connection ->
                navController.navigate("connections/${connection.id}")
            }
        }
        /**
         * When user selected one of their connections it should redirect to the profile screen of the connection user
         * And show the profile with cards that user has access to.
         */
        composable(
            "connections/{connectionId}",
            arguments = listOf(navArgument("connectionId") { type = NavType.StringType }),

        ) {backStackEntry ->
            ConnectionUserProfileScreenProvider(user, backStackEntry.arguments?.getString("connectionId"))
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
            CameraScreen(
                onBackClicked = {
                    navController.popBackStack()
                },
                onSuccessScan = {
                    navController.navigate("scanSuccess")
                }
            )
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
                canEdit = true,
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
        composable(route="scanSuccess"){
            ScanResultScreen(

            )
        }
    }
}
