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
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetBluetoothScanPermission
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetEnableBluetoothPermission
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetFineLocationPermission

@Composable()
fun ShareViaBluetoothScreenProvider(shareId: String) {
    Column {
        GetEnableBluetoothPermission()
        GetBluetoothConnectPermission()
        GetBluetoothScanPermission()
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
    val pairedDevices = appBluetoothViewModel.pairedDevices.collectAsState(initial = listOf())

    Column {
        Text(text = "isScanning: ${isScanning.value}")
        Text(text = "scanResultList.value: ${scanResultList.value.count()}")
        Text(text = "bluetoothDevicesFound.value: ${bluetoothDevicesFound.value.count()}")
        Text(text = "bluetoothDeviceConnectionStatus.value: ${appBluetoothViewModel.bluetoothDeviceConnectionStatus}")
        Button(onClick = { appBluetoothViewModel.scan() }) {
            Text(text = "Start Scan")
        }
        Button(onClick = { appBluetoothViewModel.updatePaired() }) {
            Text(text = "Update paired")
        }
        Button(onClick = { appBluetoothViewModel.startServer() }) {
            Text(text = "Start Server")
        }
        Button(onClick = { appBluetoothViewModel.sendMessage("SHARE ID") }) {
            Text(text = "Send Message")
        }
        AppBluetoothDeviceList(data = bluetoothDevicesFound.value) {
            appBluetoothViewModel.connectToDevice(it)
        }
        AppBluetoothDeviceList(data = scanResultList.value) {
            appBluetoothViewModel.connectToDevice(it)
        }
        AppBluetoothDeviceList(data = pairedDevices.value) {
            appBluetoothViewModel.connectToDevice(it)
        }
    }
}