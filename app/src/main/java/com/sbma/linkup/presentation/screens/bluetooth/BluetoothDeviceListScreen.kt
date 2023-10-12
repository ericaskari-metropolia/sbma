package com.sbma.linkup.presentation.screens.bluetooth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbma.linkup.bluetooth.connect.IBluetoothDeviceDomain
import com.sbma.linkup.presentation.icons.Bluetooth


@SuppressLint("MissingPermission")
@Composable
fun AppBluetoothDeviceListScreen(
    data: List<IBluetoothDeviceDomain>,
    modifier: Modifier = Modifier,
    onClick: (device: IBluetoothDeviceDomain) -> Unit
) {
    Scaffold(
        topBar = {
            BluetoothListScreenTopBar()
        }
    ) {padding ->

    Surface(
        modifier = modifier.padding(padding),
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(top = 36.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(data) { scanResult ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(scanResult) }
                        .padding(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row {

                        Icon(
                            Icons.Filled.Bluetooth,
                            contentDescription = "AccountCircle",
                            modifier = Modifier
                                .size(46.dp)
                                .padding(8.dp, top = 14.dp)

                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = scanResult.address,
                                fontWeight = FontWeight.Bold
                            )

                            scanResult.name?.let {
                                Text(
                                    text = it,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }



                }
            }
        }
    }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothListScreenTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "Bluetooth Lists",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}


