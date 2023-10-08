package com.sbma.linkup.presentation.screens.bluetooth.permissions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.presentation.screens.bluetooth.AppBluetoothViewModel

@Composable
fun GetEnableBluetoothPermission(
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
    ) {
    val enabled = appBluetoothViewModel.isBluetoothEnabled.collectAsState()


    if (enabled.value) {
        Text("Bluetooth is Enabled")
    } else {
        Button(onClick = { appBluetoothViewModel.askToTurnBluetoothOn() }) {
            Text(text = "Allow bluetooth")
        }
    }
}