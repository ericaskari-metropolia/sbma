package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetBluetoothConnectPermission
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetEnableBluetoothPermission
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetFineLocationPermission

@Composable()
fun ShareViaBluetoothScreenProvider(shareId: String) {
    Column {
        GetEnableBluetoothPermission()
        GetBluetoothConnectPermission()
        GetFineLocationPermission()
        ShareViaBluetoothScreen()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable()
fun ShareViaBluetoothScreen() {
Text(text = "Real deal")

}