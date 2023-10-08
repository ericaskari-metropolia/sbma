package com.sbma.linkup.application.broadcast

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.sbma.linkup.presentation.screens.bluetooth.AppBluetoothManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Main responsibility of this class is to observe Bluetooth status stop scan if it turned off.
 */
class AppBroadcastReceiver(
    private val activity: ComponentActivity,
    private val appBluetoothManager: AppBluetoothManager,
    private val bluetoothAdapter: BluetoothAdapter
) : DefaultLifecycleObserver {
     private val _bluetoothEnabled: MutableStateFlow<Boolean> = MutableStateFlow(bluetoothAdapter.isEnabled);
     val bluetoothEnabled: StateFlow<Boolean> get() = _bluetoothEnabled.asStateFlow()

    // broadcastReceiver reference. It still should be registered in onResume and unregistered on onPause lifecycle.
    private lateinit var broadcastReceiver: BroadcastReceiver

    // Intent Launcher. We use it show a popup to user to turn the bluetooth on.
    private lateinit var intentActivityResultLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        println("$TAG onCreate")

        // Initialization
        broadcastReceiver = broadcastReceiverFactory {
            onBluetoothAdapterStateChange(bluetoothAdapter.state)
        }

        // Initialization
        intentActivityResultLauncher = activityResultLauncherFactory(activity, owner, "AppAppBroadcastReceiverLauncher") {
            onBluetoothAdapterStateChange(bluetoothAdapter.state)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        println("$TAG onPause called")

        try {
            activity.unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {
            println(e)
        } finally {
            appBluetoothManager.stopScan()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        println("$TAG onResume called")
        println("$TAG registerReceiver")
        ContextCompat.registerReceiver(activity, broadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED), ContextCompat.RECEIVER_EXPORTED)
    }


    fun launchEnableBtAdapter() {
        try {
            intentActivityResultLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } catch (e: Exception) {
            println(e)
        }
    }


    companion object {
        const val TAG = "[AppBroadcastReceiver]"
        private fun broadcastReceiverFactory(onBluetoothStateChange: () -> Unit): BroadcastReceiver {
            println("$TAG broadcastReceiverFactory")
            return object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    println("$TAG broadcastReceiver onReceive ${intent.action}")

                    // It means the user has changed their bluetooth state.
                    if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                        onBluetoothStateChange()
                    }
                }
            }
        }

        private fun activityResultLauncherFactory(activity: ComponentActivity, owner: LifecycleOwner, key: String, onAccept: () -> Unit) =
            activity.activityResultRegistry.register(
                key, owner, ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val accepted: Boolean = result.resultCode == Activity.RESULT_OK
                if (accepted) {
                    // Since when intent is launched app will call on onPause and Broadcast receiver will not get the bluetooth state change event.
                    // therefore we need to call it manually.
                    onAccept()
                }
            }
    }

    private fun onBluetoothAdapterStateChange(state: Int) {
        _bluetoothEnabled.value = state == BluetoothAdapter.STATE_ON
        println("$TAG onBluetoothAdapterStateChange $state")
//
//        if (state == BluetoothAdapter.STATE_OFF) {
//            appBluetoothManager.stopScan()
//        }
//        if (state == BluetoothAdapter.STATE_ON) {
//            println("$TAG btadapter back on...")
//        }
    }
}