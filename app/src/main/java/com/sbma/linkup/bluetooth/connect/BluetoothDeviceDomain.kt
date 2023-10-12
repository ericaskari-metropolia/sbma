package com.sbma.linkup.bluetooth.connect

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice


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
