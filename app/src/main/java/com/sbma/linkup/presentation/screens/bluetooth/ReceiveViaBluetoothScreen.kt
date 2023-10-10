package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.presentation.screens.bluetooth.permissions.GetAllBluetoothPermissionsProvider
import com.sbma.linkup.user.UserViewModel
import com.sbma.linkup.util.uuidOrNull
import kotlinx.coroutines.flow.collectLatest

@Composable()
fun ReceiveViaBluetoothScreenProvider(
    appBluetoothViewModel: AppBluetoothViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSuccess: () -> Unit,

) {
    var permissionsAllowed by rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        if (!permissionsAllowed) {
            GetAllBluetoothPermissionsProvider {
                println("All permissions are good now")
                permissionsAllowed = true
            }
        } else {
            LaunchedEffect(true) {
                println("Collecting  state now!")

                appBluetoothViewModel.state.collectLatest {state ->
                    println("Collect state")
                    state.messages.lastOrNull()?.let { message ->
                    println("Collect message")
                        message.message.uuidOrNull()?.let { shareId ->
                            println("ShareId received:")
                            userViewModel.scanShareId(
                                id = shareId,
                                onSuccess = {
                                    onSuccess()
                                },
                                onFailure = {

                                }

                            )
                        }
                    }
                }
            }
            ReceiveViaBluetoothScreen {
                appBluetoothViewModel.startServer()
            }
        }
    }
}

@Composable()
fun ReceiveViaBluetoothScreen(
    startServer: () -> Unit
) {
    LaunchedEffect(true){
        startServer()
    }

    Text(text = "Waiting For Connection")
}