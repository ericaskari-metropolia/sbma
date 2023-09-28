package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbma.linkup.R
import com.sbma.linkup.ui.theme.LinkUpTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainShareScreen(onQrCodeClick: () -> Unit, onNfcClick: () -> Unit, onBluetoothClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Share",
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(5.dp)
        )
        Text(
            text = "How would you like to share?",
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            onClick = { onQrCodeClick() },
            modifier = Modifier.size(width = 150.dp, height = 150.dp),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "QR Code",
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.TopCenter)
                )
                Image(
                    painterResource(R.drawable.qr_code_icon),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            onClick = { onNfcClick() },
            modifier = Modifier.size(width = 150.dp, height = 150.dp),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "NFC", modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(20.dp)
                )
                Image(
                    painterResource(R.drawable.nfc_icon),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(70.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            onClick = { onBluetoothClick() },
            modifier = Modifier.size(width = 150.dp, height = 150.dp),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),

            ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Bluetooth", modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.TopCenter)

                )
                Image(
                    painterResource(R.drawable.bluetooth_wireless_icon),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainShareScreenPreview() {
    LinkUpTheme {
        MainShareScreen(onQrCodeClick = {}, onNfcClick = {}, onBluetoothClick = {})
    }
}
