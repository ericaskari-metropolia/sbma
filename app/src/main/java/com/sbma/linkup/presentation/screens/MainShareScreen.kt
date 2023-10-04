package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.sbma.linkup.R
import com.sbma.linkup.ui.theme.LinkUpTheme

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainShareScreen(
    onQrCodeClick: () -> Unit,
    isReceiving: Boolean,
    onNfcClick: () -> Unit,
    onBluetoothClick: () -> Unit,
    onBackClick: () -> Unit

) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isReceiving) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackClick() }
                )
            }
        }

        Text(
            text = if (isReceiving) "Receive" else "Share",
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(5.dp)
        )
        Text(
            text = if (isReceiving) "How would you like to receive?" else "How would you like to share?",
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
                    contentDescription = "QR code",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp),
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
                    contentDescription = "Bluetooth",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(50.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = if (isReceiving) "Or would you like to receive from a card or tag?" else "Or would you like to assign a card or tag?",
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            lineHeight = 1.5.em,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            onClick = { onNfcClick() },
            modifier = Modifier.size(width = 150.dp, height = 150.dp),
            shape = MaterialTheme.shapes.medium.copy(all = CornerSize(0.dp)),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painterResource(R.drawable.nfcphone),
                    contentDescription = "Nfc",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(150.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MainShareScreenPreview() {
    LinkUpTheme {
        MainShareScreen(
            onQrCodeClick = {},
            onNfcClick = {},
            onBluetoothClick = {},
            isReceiving = true,
            onBackClick = {}
        )
    }
}
