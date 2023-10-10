package com.sbma.linkup.application

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.nfc.NfcAdapter
import androidx.activity.ComponentActivity
import com.sbma.linkup.application.broadcast.AppBroadcastReceiver
import com.sbma.linkup.application.connectivity.AppConnectivityManager
import com.sbma.linkup.application.connectivity.InternetConnectionState
import com.sbma.linkup.application.data.AppContainer
import com.sbma.linkup.application.data.AppDataContainer
import com.sbma.linkup.datasource.DataStore
import com.sbma.linkup.nfc.AppNfcManager
import com.sbma.linkup.presentation.screens.bluetooth.AppBluetoothManager
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

    // Service to save key value format data
    lateinit var dataStore: DataStore

    // Service to check internet connection
    lateinit var appConnectivityManager: AppConnectivityManager

    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var appBluetoothManager: AppBluetoothManager
    lateinit var appBroadcastReceiver: AppBroadcastReceiver;
    lateinit var appNfcManager: AppNfcManager
    var nfcAdapter: NfcAdapter? = null


    // Container of repositories
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        dataStore = DataStore(this)

        appConnectivityManager = AppConnectivityManager(this) {
            println("$TAG[AppConnectivityManager] ${it.toTitle()}")
            internetConnectionState.value = it
        }

        container = AppDataContainer(this)

        bluetoothManager = getSystemService(BluetoothManager::class.java)

        bluetoothAdapter = bluetoothManager.adapter
        appBluetoothManager = AppBluetoothManager(
            bluetoothAdapter = bluetoothAdapter,
        )
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        coroutineScope.launch {
            val state = internetConnectionState.first()
            if (state.isConnected()) {
                // Call Apis when app launches.
            } else {
                println("Skipping api calls since there is no internet")
            }
        }


    }

    fun initAppNfcManager(activity: ComponentActivity): AppNfcManager {
        appNfcManager = AppNfcManager(this, activity, nfcAdapter)
        return appNfcManager
    }
    fun initAppBroadcastReceiver(activity: ComponentActivity): AppBroadcastReceiver {
        appBroadcastReceiver = AppBroadcastReceiver(
            activity = activity,
            bluetoothAdapter = bluetoothAdapter,
            appBluetoothManager = appBluetoothManager,
        )

        return appBroadcastReceiver
    }

}