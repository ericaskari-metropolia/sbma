package com.sbma.linkup.EditProfile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.presentation.screens.CreateCard
import com.sbma.linkup.presentation.screens.CreateCardData
import com.sbma.linkup.ui.theme.LinkUpTheme
import com.sbma.linkup.ui.theme.YellowApp
import com.sbma.linkup.usercard.UserCardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onSave: () -> Unit,
    userCardViewModel: UserCardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var username by rememberSaveable { mutableStateOf("Enter Username") }
    var email by rememberSaveable { mutableStateOf("Enter your Email") }
    var note by rememberSaveable { mutableStateOf("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ") }
    var phone by rememberSaveable { mutableStateOf("Add your phone number") }
    var address by rememberSaveable { mutableStateOf("Add your address") }
    var instagram by rememberSaveable { mutableStateOf("Instagram account") }
    var twitter by rememberSaveable { mutableStateOf("Twitter account") }
    var linkedIn by rememberSaveable { mutableStateOf("LinkedIn account") }
    var facebook by rememberSaveable { mutableStateOf("Facebook account") }
    var newCards = remember { mutableStateOf<List<CreateCardData>>(listOf()) }
    val composableScope = rememberCoroutineScope()



    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "Back",
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Edit Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.profile_photo),
                contentDescription = "profile photo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .scale(2f)
            )
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }

//        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sebubebu", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "UX/UI Designer", fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        //Username
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
        }

        //Email
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Email", modifier = Modifier.width(100.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
        }

        //Phone Number
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Phone number", modifier = Modifier.width(100.dp))
            TextField(
                value = phone,
                onValueChange = { phone = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
        }

        //Address
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Address", modifier = Modifier.width(100.dp))
            TextField(
                value = address,
                onValueChange = { address = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
        }

        //Note
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
        ) {
            Text(
                text = "Note", modifier = Modifier
                    .width(100.dp)
                    .padding(top = 8.dp)
            )
            TextField(
                value = note,
                onValueChange = { note = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                ),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }

        Text(text = newCards.value.count().toString())

        CreateCard(onSubmit = {
            val copy = newCards.value.toMutableList()
            copy.add(it)
            newCards.value = copy
            println("CreateCard")

        })

        Column {

//            //Instagram
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 4.dp, end = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painterResource(R.drawable.instagram),
//                    contentDescription = "Edit",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(40.dp)
//                )
//                TextField(
//                    value = instagram,
//                    onValueChange = { instagram = it },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent
//                    )
//                )
//
//            }
//
//            //Twitter
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 4.dp, end = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painterResource(R.drawable.twitter),
//                    contentDescription = "Edit",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(40.dp)
//                )
//                TextField(
//                    value = twitter,
//                    onValueChange = { twitter = it },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent
//                    )
//                )
//
//            }
//
//            //LinkedIn
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 4.dp, end = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painterResource(R.drawable.linkedin),
//                    contentDescription = "Edit",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(40.dp)
//                )
//                TextField(
//                    value = linkedIn,
//                    onValueChange = { linkedIn = it },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent
//                    )
//                )
//            }
//
//            //Facebook
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 4.dp, end = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painterResource(R.drawable.facebook),
//                    contentDescription = "Edit",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.size(40.dp)
//                )
//                TextField(
//                    value = facebook,
//                    onValueChange = { facebook = it },
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent
//                    )
//                )
//            }
        }
        Column {
            newCards.value.forEach() {
                Row(
                ) {
                    Image(
                        painterResource(R.drawable.twitter),
                        contentDescription = "Edit",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(it.value)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Button(
                modifier = Modifier.padding(bottom = 100.dp),
                onClick = {
                    composableScope.launch {
                        userCardViewModel.saveItems(newCards.value)
                        onSave()
                    }
                },
                colors = buttonColors(YellowApp)
            ) {
                Text("Save", color = Color.Black)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LinkUpTheme {
        EditProfileScreen(onSave = {})
    }
}