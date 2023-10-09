package com.sbma.linkup.presentation.screens.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbma.linkup.application.broadcast.AppBroadcastReceiver
import com.sbma.linkup.presentation.screens.bluetooth.connect.BluetoothDeviceDomain
import com.sbma.linkup.presentation.screens.bluetooth.connect.ConnectionResult
import com.sbma.linkup.presentation.screens.bluetooth.connect.FoundedBluetoothDeviceDomain
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}
@SuppressLint("MissingPermission")
fun BluetoothDevice.toFoundedBluetoothDeviceDomain(lastSeen: Long): FoundedBluetoothDeviceDomain {
    return FoundedBluetoothDeviceDomain(
        name = name,
        address = address,
        lastSeen = lastSeen
    )
}

@SuppressLint("MissingPermission")
fun ScanResult.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = device.name,
        address = device.address
    )
}

class AppBluetoothViewModel(
    private val appBluetoothManager: AppBluetoothManager,
    private val appBroadcastReceiver: AppBroadcastReceiver,
) : ViewModel() {
    val isBluetoothEnabled = appBroadcastReceiver.bluetoothEnabled
    val bluetoothDevicesFound = appBroadcastReceiver.bluetoothDevicesFound
    val scanResultList = appBluetoothManager.scanResultList
    val isScanning = appBluetoothManager.isScanning

    private var deviceConnectionJob: Job? = null

    private val _state = MutableStateFlow(BluetoothUiState())

    val state = combine(
        appBluetoothManager.scannedDevices,
        appBluetoothManager.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    fun scan() {
        if (isScanning.value) {
            return
        }
        viewModelScope.launch {
            appBluetoothManager.scan()
            delay(5000)
            appBluetoothManager.stopScan()
        }
    }

    fun askToTurnBluetoothOn() {
        appBroadcastReceiver.launchEnableBtAdapter()
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        println("connectToDevice: ${device}")
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = appBluetoothManager
            .connectToDevice(device)
            .listen()
    }


    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            println("Flow listen: ${result}")

            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
                        )
                    }
                }

                is ConnectionResult.TransferSucceeded -> {
                    _state.update {
                        it.copy(
                            messages = it.messages + result.message
                        )
                    }
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
            .catch { throwable ->
                println("Flow listen catch")
                println(throwable)
                appBluetoothManager.closeConnection()
                _state.update {
                    it.copy(
                        isConnected = false,
                        isConnecting = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun startServer() {
        appBluetoothManager.startBluetoothServer().listen()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = appBluetoothManager.trySendMessage(message)
            if(bluetoothMessage != null) {
                _state.update { it.copy(
                    messages = it.messages + bluetoothMessage
                ) }
            }
        }
    }
//    fun getReadBytes(): ByteArray? {
//        return _services.value.let { svcs ->
//            svcs.flatMap { it.characteristics }.find {
//                it.uuid == ELKBLEDOM.uuid
//            }?.readBytes
//        }
//    }
//    fun getOnOffState(): Boolean {
//        val bytes = getReadBytes()
//        return bytes?.let {
//            ELKBLEDOM.onOffState(it)
//        } ?: false
//    }
}