package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.presentation.components.UserCardsList
import com.sbma.linkup.ui.theme.LinkUpTheme
import com.sbma.linkup.user.User
import com.sbma.linkup.usercard.UserCard
import com.sbma.linkup.usercard.UserCardViewModel
import java.util.UUID

@Composable
fun UserProfileScreenProvider(user: User, onShareClick: () -> Unit, onEditClick: () -> Unit) {
    val userCardViewModel: UserCardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userCards = userCardViewModel.allItemsStream(user.id).collectAsState(initial = listOf())
    UserProfileScreen(user, userCards.value, onEditClick = onEditClick, onShareClick = onShareClick)
}

@Composable
fun UserProfileScreen(user: User, userCards: List<UserCard>, onShareClick: () -> Unit, onEditClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ScreenTitle(
            onEditClick = onEditClick,
            onShareClick = onShareClick
        )
        Column {
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
                    AsyncImage(
                        model = user.picture,
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
                    Text(text = user.name, fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = user.description, fontSize = 15.sp)
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
                            Text(
                                text = "About Me",
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                            )
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
                        Text(
                            text = "Contact Details",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                        )
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
                UserCardsList(userCards, withLazyColumn = false)
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
fun ScreenTitle(onShareClick: () -> Unit, onEditClick: () -> Unit) {
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
        Row {

            Icon(
                Icons.Filled.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20))
                    .clickable {
                        onEditClick()
                    }
            )
            Icon(
                Icons.Filled.Share,
                contentDescription = "Edit",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(20))
                    .clickable {
                        onShareClick()
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val user = remember { mutableStateOf(User(UUID.randomUUID(), "Sebubebu", "UX/UI Designer", null)) }
    val cards = remember {
        mutableListOf(
            UserCard(UUID.randomUUID(), user.value.id, "Facebook", "https://facebook.com/something"),
            UserCard(UUID.randomUUID(), user.value.id, "Instagram", "https://instagram.com/something"),
        )
    }
    LinkUpTheme {
        UserProfileScreen(
            user.value,
            cards,
            onShareClick = {},
            onEditClick = {}
        )
    }
}

