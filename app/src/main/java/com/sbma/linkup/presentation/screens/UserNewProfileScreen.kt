package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sbma.linkup.presentation.screenstates.UserNewProfileScreenState
import com.sbma.linkup.ui.theme.LinkUpTheme

@Composable
fun NewProfileScreen(onSubmit: (value: UserNewProfileScreenState) -> Unit) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var instagramLink by remember { mutableStateOf("") }
    var twitterLink by remember { mutableStateOf("") }
    var facebookLink by remember { mutableStateOf("") }
    var linkedinLink by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .padding(10.dp),
        content = {
            item {
                Text(
                    text = "Create a new profile",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            item {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("About Me") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                Text(
                    text = "Social Media Links",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            item {
                TextField(
                    value = instagramLink,
                    onValueChange = { instagramLink = it },
                    label = { Text("Instagram") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = twitterLink,
                    onValueChange = { twitterLink = it },
                    label = { Text("Twitter") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = facebookLink,
                    onValueChange = { facebookLink = it },
                    label = { Text("Facebook") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                TextField(
                    value = linkedinLink,
                    onValueChange = { linkedinLink = it },
                    label = { Text("Linkedin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                Button(
                    onClick = {
                        onSubmit(
                            UserNewProfileScreenState(
                                username = username,
                                email = email,
                                phoneNumber = phoneNumber,
                                address = address,
                                description = description,
                                instagramLink = instagramLink,
                                twitterLink = twitterLink,
                                facebookLink = facebookLink,
                                linkedinLink = linkedinLink
                            )
                        )
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Save")
                }
            }
        }
    )

}

@Preview(showBackground = true)
@Composable
fun NewProfileScreenPreview() {
    LinkUpTheme {
        NewProfileScreen {

        }
    }
}