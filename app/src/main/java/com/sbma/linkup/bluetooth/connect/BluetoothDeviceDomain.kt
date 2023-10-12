package com.sbma.linkup.bluetooth.connect

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

interface IBluetoothDeviceDomain {
    val address: String
    val name: String?
}

data class BluetoothDeviceDomain (
    override val address: String,
    override val name: String?,
): IBluetoothDeviceDomain

interface IFoundedBluetoothDeviceDomain: IBluetoothDeviceDomain {
    val lastSeen: Long
}
data class FoundedBluetoothDeviceDomain (
    override val address: String,
    override val name: String?,
    override val lastSeen: Long

): IFoundedBluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
