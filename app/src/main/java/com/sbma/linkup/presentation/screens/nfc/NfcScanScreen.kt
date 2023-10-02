package com.sbma.linkup.presentation.screens.nfc

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.nfc.NFCViewModel

@Composable
fun NfcScanScreen(
    nfcViewModel: NFCViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val toastMessage = nfcViewModel.observeToast().collectAsState(initial = null)
    val tagInfo = nfcViewModel.observeTag().collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        toastMessage.value?.let {
            Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
        }
        tagInfo.value?.let {
            Text("Tag Info: $it")
        }
        val checked = remember {
            mutableStateOf(false)
        }
        Switch(
            checked = checked.value, // Toggle NFC
            onCheckedChange = { isChecked ->
                checked.value = isChecked
                nfcViewModel.onCheckNFC(isChecked)
            }
        )
    }
}