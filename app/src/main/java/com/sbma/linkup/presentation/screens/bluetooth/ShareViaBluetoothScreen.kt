package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.sbma.linkup.application.data.AppViewModelProvider
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
fun ShareViaBluetoothScreen(
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
    ) {
    val isScanning = appBluetoothViewModel.isScanning.collectAsState()
    val scanResultList = appBluetoothViewModel.scanResultList.collectAsState(initial = listOf())
    val bluetoothDevicesFound = appBluetoothViewModel.bluetoothDevicesFound.collectAsState(initial = listOf())

    Column {
        Text(text = "isScanning: ${isScanning.value}")
        Button(onClick = { appBluetoothViewModel.scan() }) {
            Text(text = "Start Scan")
        }
        Button(onClick = { appBluetoothViewModel.startServer() }) {
            Text(text = "Start Server")
        }
        AppBluetoothDeviceList(data = bluetoothDevicesFound.value) {
        }
        AppBluetoothDeviceList(data = scanResultList.value) {
        }
    }
}