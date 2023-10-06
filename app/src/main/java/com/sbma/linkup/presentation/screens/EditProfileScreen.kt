package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.presentation.components.UserCardsList
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.presentation.ui.theme.YellowApp
import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    user: User,
    onSave: () -> Unit,
    userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var aboutMe by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var instagram by rememberSaveable { mutableStateOf("Instagram account") }
    var twitter by rememberSaveable { mutableStateOf("Twitter account") }
    var linkedIn by rememberSaveable { mutableStateOf("LinkedIn account") }
    var facebook by rememberSaveable { mutableStateOf("Facebook account") }
    var newCards = remember { mutableStateOf<List<Card>>(listOf()) }
    val composableScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        userCardViewModel.allItemsStream(user.id).collectLatest {
            println(it)
            var phoneNumberCard = it.find { it.title == "Phone Number" }
            println(phoneNumberCard)
            phoneNumberCard?.let {
                phone = it.value
            }
        }
    }

    LaunchedEffect(true) {
        userCardViewModel.allItemsStream(user.id).collectLatest {
            println(it)
            var addressCard = it.find { it.title == "Address" }
            println(addressCard)
            addressCard?.let {
                address = it.value
            }
        }
    }

    LaunchedEffect(true) {
        userCardViewModel.allItemsStream(user.id).collectLatest {
            println(it)
            var aboutMeCard = it.find { it.title == "About Me" }
            println(aboutMeCard)
            aboutMeCard?.let {
                aboutMe = it.value
            }
        }
    }

    LaunchedEffect(true) {
        userCardViewModel.allItemsStream(user.id).collectLatest {
            println(it)
            var descriptionCard = it.find { it.title == "Description" }
            println(descriptionCard)
            descriptionCard?.let {
                description = it.value
            }
        }
    }

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
            AsyncImage(
                model = user.picture,
                contentDescription = "profile photo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(50.dp))
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = user.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        //Description
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Description",
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp),
                fontWeight = FontWeight.Bold,
            )
            TextField(
                value = description,
                onValueChange = { description = it },
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
            Text(
                text = "Phone number",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp),
                fontWeight = FontWeight.Bold,
            )
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
            Text(
                text = "Address",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp),
                fontWeight = FontWeight.Bold,
            )
            TextField(
                value = address,
                onValueChange = { address = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
        }

        // About Me
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
        ) {
            Text(
                text = "About Me",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp),
                fontWeight = FontWeight.Bold,
            )
            TextField(
                value = aboutMe,
                onValueChange = { aboutMe = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                ),
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

        CreateCard(onSubmit = {
            val copy = newCards.value.toMutableList()
            copy.add(Card(id= UUID.randomUUID(), userId = user.id, title = it.title, value = it.value, picture = null))
            newCards.value = copy
            println("CreateCard")

        })

        Column {
            UserCardsList(newCards.value, withLazyColumn = false)
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
        BottomSheet()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet() {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                isSheetOpen = true
            },
        ) {
            Text(text = "Add social media platform")
        }

    }
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false }) {
            SocialMediaList()
        }
    }


}

@Composable
fun SocialMediaItem(
    iconRes: Int,
    text: String
) {
    Column {
        ListItem(
            headlineContent = {
                Text(
                    text = text,
                )
            },
            leadingContent = {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        )
    }

}

@Composable
fun SocialMediaList() {
    val socialMediaList = listOf(
        Pair(R.drawable.call, "Phone Number"),
        Pair(R.drawable.description, "Description"),
        Pair(R.drawable.aboutme, "About Me"),
        Pair(R.drawable.location, "Location"),
        Pair(R.drawable.instagram, "Instagram"),
        Pair(R.drawable.twitter, "Twitter"),
        Pair(R.drawable.linkedin, "Linkedin"),
        Pair(R.drawable.facebook, "Facebook"),
        Pair(R.drawable.snapchat, "Snapchat"),
        Pair(R.drawable.pinterest, "Pintrest"),
        Pair(R.drawable.telegram, "Telegram"),
        Pair(R.drawable.tiktok, "Tiktok"),
        Pair(R.drawable.youtube, "Youtube"),
        Pair(R.drawable.discord, "Discord"),
        Pair(R.drawable.github, "Github"),
    )

    val rows = socialMediaList.chunked(3)

    LazyColumn {
        items(rows) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for ((iconRes, text) in rowItems) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val user =
        remember {
            mutableStateOf(
                User(
                    UUID.randomUUID(),
                    "Sebubebu",
                    email = "",
                    description = "UX/UI Designer",
                    picture = null
                )
            )
        }
    LinkUpTheme {
        EditProfileScreen(user.value, onSave = {})

    }
}

