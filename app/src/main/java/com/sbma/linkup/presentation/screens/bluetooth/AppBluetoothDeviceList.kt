package com.sbma.linkup.presentation.screens.bluetooth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sbma.linkup.presentation.screens.bluetooth.connect.IBluetoothDeviceDomain

@SuppressLint("MissingPermission")
@Composable
fun AppBluetoothDeviceList(data: List<IBluetoothDeviceDomain>, modifier: Modifier = Modifier, onClick: (device: IBluetoothDeviceDomain) -> Unit) {
    LazyColumn(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        items(data) { scanResult ->

            ListItem(
                modifier = Modifier
                    .clickable { onClick(scanResult) },

                overlineContent = { Text(scanResult.address) },
                headlineContent = { scanResult.name?.let { Text(it) } },
                leadingContent = {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "AccountCircle",
                    )
                }
            )
        }
    }

}