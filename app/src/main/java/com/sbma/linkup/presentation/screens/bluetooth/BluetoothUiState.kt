package com.sbma.linkup.presentation.screens.bluetooth

import com.sbma.linkup.bluetooth.connect.BluetoothMessage
import com.sbma.linkup.bluetooth.connect.IBluetoothDeviceDomain


data class BluetoothUiState(
    val scannedDevices: List<IBluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<IBluetoothDeviceDomain> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)
