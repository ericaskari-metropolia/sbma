package com.sbma.linkup.bluetooth.connect

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult

interface IdentifiableAppBluetoothDevice {
    val address: String
}



data class BluetoothDeviceDomain (
     val address: String,
     val name: String?,
)

data class FoundedBluetoothDeviceDomain (
     val address: String,
     val name: String?,
     val lastSeen: Long

)

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}

@SuppressLint("MissingPermission")
fun ScanResult.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = device.name,
        address = device.address
    )
}
