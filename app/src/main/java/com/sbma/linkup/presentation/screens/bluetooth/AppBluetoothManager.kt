package com.sbma.linkup.presentation.screens.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import com.sbma.linkup.presentation.screens.bluetooth.connect.BluetoothDataTransferService
import com.sbma.linkup.presentation.screens.bluetooth.connect.BluetoothDeviceDomain
import com.sbma.linkup.presentation.screens.bluetooth.connect.BluetoothMessage
import com.sbma.linkup.presentation.screens.bluetooth.connect.ConnectionResult
import com.sbma.linkup.presentation.screens.bluetooth.connect.toBluetoothDeviceDomain
import com.sbma.linkup.presentation.screens.bluetooth.connect.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class AppBluetoothManager(
    val bluetoothAdapter: BluetoothAdapter?,
    private val scope: CoroutineScope,
    private val context: Context
) {
    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null
    private var dataTransferService: BluetoothDataTransferService? = null

    val scanResultList = MutableStateFlow(mapOf<String, BluetoothDeviceDomain>())
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean>
        get() = _isScanning.asStateFlow()


    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()



    private var lastCleanupTimestamp: Long? = null
    private val CLEANUP_DURATION = 10000L

    //  Configuration for bluetooth scanning


    private val scanCallback = appBluetoothScanCallbackFactory(
        onScanResult = {
            scope.launch {
                val copy = scanResultList.value.toMutableMap()
                copy[it.device.address] = it.toBluetoothDeviceDomain()
                scanResultList.value = copy

                if (_isScanning.value) {
                    launch { deleteNotSeen() }
                }
            }
        },
        onScanFailed = { errorCode ->
            println("[$TAG] onScanFailed")
            println("Scan error $errorCode")

            if (errorCode == ScanCallback.SCAN_FAILED_ALREADY_STARTED) {
                stopScan()
                scan()
            }
        }
    )

    suspend fun deleteNotSeen() {
        val prefix = "[$TAG][deleteNotSeen]"
        lastCleanupTimestamp?.let {
            if (System.currentTimeMillis() - it > CLEANUP_DURATION) {
//                bluetoothDeviceRepository.deleteNotSeen()
                println("$prefix deleted not seen")
                lastCleanupTimestamp = System.currentTimeMillis()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun scan() {
        val prefix = "[$TAG][scan]"
        println(prefix)

        if (bluetoothAdapter == null) {
            return
        }
        if (!_isScanning.value) {
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            return
        }

        _isScanning.value = true

        try {
            bluetoothAdapter.bluetoothLeScanner.startScan(null, scanSettings, scanCallback)
            lastCleanupTimestamp = System.currentTimeMillis()

        } catch (e: Exception) {
            println("scan ERROR")
            println(e)
            println(e.message)
            _isScanning.value = false
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        println("[$TAG] stopScan")
        if (!_isScanning.value) {
            return
        }
        bluetoothAdapter?.let {
            _isScanning.value = false
            try {
                bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun startBluetoothServer(): Flow<ConnectionResult> {
        return flow {
            println("[startBluetoothServer] listenUsingRfcommWithServiceRecord")
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service",
                UUID.fromString(SERVICE_UUID)
            )

            var shouldLoop = true
            while (shouldLoop) {
                println("[startBluetoothServer] shouldLoop")

                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    println("[startBluetoothServer] e")
                    println(e)
                    shouldLoop = false
                    null
                }

                println("[startBluetoothServer] ConnectionEstablished")
                emit(ConnectionResult.ConnectionEstablished)
                currentClientSocket?.let {
                    println("[startBluetoothServer] currentServerSocket?.close")
                    currentServerSocket?.close()
                    val service = BluetoothDataTransferService(it)
                    dataTransferService = service

                    println("[startBluetoothServer] emitAll")
                    emitAll(
                        service
                            .listenForIncomingMessages()
                            .map {
                                ConnectionResult.TransferSucceeded(it)
                            }
                    )
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }
    @SuppressLint("MissingPermission")
    suspend fun trySendMessage(message: String): BluetoothMessage? {

        if(dataTransferService == null) {
            return null
        }

        val bluetoothMessage = BluetoothMessage(
            message = message,
            senderName = bluetoothAdapter?.name ?: "Unknown name",
            isFromLocalUser = true
        )

        dataTransferService?.sendMessage(bluetoothMessage.toByteArray())

        return bluetoothMessage
    }

    @SuppressLint("MissingPermission")
    fun stopDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {
        println("[connectToDevice]")
        return flow {
//            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
//                throw SecurityException("No BLUETOOTH_CONNECT permission")
//            }

            val btDevice = bluetoothAdapter?.getRemoteDevice(device.address)
            println(btDevice)
            println("connectToDevice createRfcommSocketToServiceRecord")
            currentClientSocket = btDevice?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))
            stopDiscovery()

            currentClientSocket?.let { socket ->
                try {
                    println("connectToDevice connect")
                    socket.connect()
                    println("connectToDevice emit")
                    emit(ConnectionResult.ConnectionEstablished)

                    BluetoothDataTransferService(socket).also {
                        dataTransferService = it
                        emitAll(
                            it.listenForIncomingMessages()
                                .map { ConnectionResult.TransferSucceeded(it) }
                        )
                    }
                } catch (e: IOException) {
                    println("currentClientSocket e")
                    println(e)
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.Error("Connection was interrupted"))
                }
            }
        }.onCompletion {
            println(it)
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    fun closeConnection() {
        println("$TAG closeConnection")
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentClientSocket = null
        currentServerSocket = null
    }

    companion object {
        const val TAG = "[AppBluetoothManager]"
        const val SERVICE_UUID = "27b7d1da-08c7-4505-a6d1-2459987e5e2d"

        private val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()


        private fun appBluetoothScanCallbackFactory(
            onScanFailed: (errorCode: Int) -> Unit,
            onScanResult: (result: ScanResult) -> Unit,
        ): ScanCallback {
            return object : ScanCallback() {
                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    onScanFailed(errorCode)
                }

                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    super.onScanResult(callbackType, result)
                    onScanResult(result)
                }
            }
        }
    }
}