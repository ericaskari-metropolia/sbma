package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbma.linkup.R
import com.sbma.linkup.ui.theme.LinkUpTheme

@Composable
fun ProfileScreen() {
    ScreenTitle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column{
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier
                        .padding(all = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Image(
                        painter = painterResource(R.drawable.profile_photo),
                        contentDescription = "profile photo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .scale(2f)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Sebubebu", fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "UX/UI Designer", fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .size(width = 400.dp, height = 240.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(text = "About Me",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Sebubebu is a results-driven Marketing Manager with a passion " +
                                        "for leveraging innovative strategies to drive brand growth and " +
                                        "customer engagement in the ever-evolving digital landscape." +
                                        "Feel free to reach me out for any specific queries.",
                                fontSize = 16.sp,
                            )
                        }

                    }
                }
                Card(
                    modifier = Modifier
                        .size(width = 400.dp, height = 220.dp)
                        .padding(15.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(text = "Contact Details",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,)
                        Spacer(modifier = Modifier.height(8.dp))
                        ContactInfoRow(
                            icon = Icons.Filled.Call,
                            text = "+358 1234567890"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ContactInfoRow(
                            icon = Icons.Filled.Email,
                            text = "sebubebu@gmail.com"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ContactInfoRow(
                            icon = Icons.Filled.LocationOn,
                            text = "Pohjoisesplanadi 31, 00100 Helsinki, Finland"
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ContactInfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()

        )
    }
}

@Composable
fun ScreenTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = "Profile",
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier

        )
        Icon(
            Icons.Filled.Edit,
            contentDescription = "Edit",
            modifier = Modifier
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    LinkUpTheme {
        ProfileScreen()
    }
}

