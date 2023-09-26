package com.sbma.linkup.application

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import com.sbma.linkup.application.connectivity.AppConnectivityManager
import com.sbma.linkup.application.connectivity.InternetConnectionState
import com.sbma.linkup.application.data.AppContainer
import com.sbma.linkup.application.data.AppDataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MyApplication : Application() {
    val TAG = "[MyApplication]"

    companion object {
        private val parentJob = Job()
        private val coroutineScope = CoroutineScope(Dispatchers.Default + parentJob)
    }

    val internetConnectionState = MutableStateFlow(InternetConnectionState.UNKNOWN)


    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */

    // Service to check internet connection
    lateinit var appConnectivityManager: AppConnectivityManager

    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter

    // Container of repositories
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        appConnectivityManager = AppConnectivityManager(this) {
            println("$TAG[AppConnectivityManager] ${it.toTitle()}")
            internetConnectionState.value = it
        }

        container = AppDataContainer(this)

        bluetoothManager = getSystemService(BluetoothManager::class.java)

        bluetoothAdapter = bluetoothManager.adapter


        coroutineScope.launch {
            val state = internetConnectionState.first()
            if (state.isConnected()) {
                // Call Apis when app launches.
            } else {
                println("Skipping api calls since there is no internet")
            }
        }

    }
}