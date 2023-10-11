package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetAllBluetoothPermissionsProvider
import kotlinx.coroutines.flow.collectLatest

@Composable()
fun ShareViaBluetoothScreenProvider(
    shareId: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    var permissionsAllowed by rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        if (!permissionsAllowed) {
            GetAllBluetoothPermissionsProvider {
                println("All permissions are good now")
                permissionsAllowed = true
            }
        } else {
            var idSent by rememberSaveable {
                mutableStateOf(false)
            }
            LaunchedEffect(true){
                appBluetoothViewModel.scan()
                appBluetoothViewModel.updatePaired()
            }
            LaunchedEffect(true) {
                println("Collecting  state now!")

                appBluetoothViewModel.state.collectLatest {state ->
                    if (idSent) {
                        return@collectLatest
                    }
                    if (state.errorMessage != null) {
                        onFailure()
                        return@collectLatest
                    }
                    println("Collect state")
                    if (state.isConnected) {
                        appBluetoothViewModel.sendMessage(shareId)
                        idSent = true
                        println("ShareId sent!")
                        onSuccess()
                    }
                }
            }
            ShareViaBluetoothScreen()
        }
    }
}

@Composable()
fun ShareViaBluetoothScreen(
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
    ) {
    val bluetoothDevicesFound = appBluetoothViewModel.bluetoothDevicesFound.collectAsState(initial = listOf())
    val pairedDevices = appBluetoothViewModel.pairedDevices.collectAsState(initial = listOf())

    Column {
        AppBluetoothDeviceList(data = bluetoothDevicesFound.value + pairedDevices.value) {
            appBluetoothViewModel.connectToDevice(it)
        }
    }
}