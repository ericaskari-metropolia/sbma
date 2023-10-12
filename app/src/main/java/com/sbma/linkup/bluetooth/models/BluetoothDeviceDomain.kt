package com.sbma.linkup.bluetooth.models

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

data class BluetoothDeviceDomain(
    val address: String,
    val name: String?,
)

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
