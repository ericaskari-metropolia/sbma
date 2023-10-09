package com.sbma.linkup.presentation.screens

import android.icu.util.ULocale
import android.icu.util.ULocale.Category
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
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
import com.sbma.linkup.util.toPictureResource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
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


        Column {
            newCards.value.forEach {
                CreateCard(it.title, it.picture ?: "")
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
        BottomSheet() { text, picture ->
            println("Clicked $text $picture")
            val copy = newCards.value.toMutableList()
            copy.add(
                Card(
                    id = UUID.randomUUID(),
                    userId = user.id,
                    title = text,
                    value = "",
                    picture = picture
                )
            )
            newCards.value = copy
            println("CreateCard")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onClick: (text: String, picture: String) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        FloatingActionButton(
            onClick = { isSheetOpen = true },
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, "Floating action button.")
        }

    }
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false }) {
            SocialMediaList() { text, picture ->
                println("Clicked! $text  $picture")
                onClick(text, picture)
                isSheetOpen = false

            }
        }
    }


}

@Composable
fun ProfileCardListItem(
    iconRes: Int,
    text: String
) {
    Column {
        ListItem(
            headlineContent = {
                Text(
                    text = text
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

data class NewCardItem(
    var category: String,
    var picture: String,
    var text: String
)

@Composable
fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SocialMediaList(onClick: (text: String, picture: String) -> Unit) {

    val newCardList = listOf(
        NewCardItem("Contact", "phone", "Phone Number"),
        NewCardItem("Contact", "description", "Description"),
        NewCardItem("Contact", "about", "About Me"),
        NewCardItem("Contact", "location", "Location"),
        NewCardItem("Social Media", "instagram", "Instagram"),
        NewCardItem("Social Media", "twitter", "Twitter"),
        NewCardItem("Social Media", "facebook", "Facebook"),
        NewCardItem("Social Media", "snapchat", "Snapchat"),
        NewCardItem("Social Media", "pinterest", "Pinterest"),
        NewCardItem("Social Media", "linkedin", "Linkedin"),
        NewCardItem("Social Media", "telegram", "Telegram"),
        NewCardItem("Social Media", "tiktok", "Tiktok"),
        NewCardItem("Social Media", "youtube", "Youtube"),
        NewCardItem("Social Media", "discord", "Discord"),
        NewCardItem("Social Media", "github", "Github"),

        )


    val categories = newCardList.groupBy { it.category }


    LazyColumn() {
        categories.forEach { category ->
            stickyHeader {
                CategoryHeader(category.key)
            }
            items(category.value.toList().chunked(3)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    for (rowItem in rowItems) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onClick(rowItem.text, rowItem.picture) }
                        ) {
                            Image(
                                painter = painterResource(rowItem.picture.toPictureResource()),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = rowItem.text,
                                modifier = Modifier.padding(start = 16.dp),
                            )
                        }
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

