package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.presentation.components.UserCardsList
import com.sbma.linkup.user.User
import java.util.UUID

@Composable
fun ConnectionUserProfileScreenProvider(user: User, connectionIdParam: String?) {
    val userConnectionViewModel: ConnectionViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val connectionUser = userConnectionViewModel.allItemsStream(user.id).collectAsState(initial = null)

    val connection = (connectionUser.value ?: mapOf()).entries.toList().find { mapEntry ->
        connectionIdParam?.let { mapEntry.key.id == UUID.fromString(it) } ?: false
    }
    connection?.let {
        val userCards = userCardViewModel.allItemsStream(it.value.id).collectAsState(initial = listOf())
        UserProfileScreen(it.value, userCards.value, canEdit = false, onEditClick = null)
    }
}

@Composable
fun UserProfileScreen(user: User, userCards: List<Card>, canEdit: Boolean, onEditClick: (() -> Unit)? = null) {
    val aboutMeCard = userCards.find { it.title == "About Me" }
    val phoneNumberCard = userCards.find { it.title == "Phone Number" }
    val emailCard = userCards.find { it.title == "Email" }
    val addressCard = userCards.find { it.title == "Location" }
    val titleCard = userCards.find { it.title == "Title" }
    val restOfTheCards = userCards
        .asSequence()
        .filter { it.title != "About Me" }
        .filter { it.title != "Phone Number" }
        .filter { it.title != "Email" }
        .filter { it.title != "Location" }
        .filter { it.title != "Title" }
        .toList()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ScreenTitle(
            canEdit = canEdit,
            onEditClick = onEditClick,
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
                    )
                }
                Text(text = user.name, fontSize = 30.sp)
                Spacer(modifier = Modifier.height(8.dp))

                titleCard?.let {
                    Text(text = it.value, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                aboutMeCard?.let {
                    Card(
                        modifier = Modifier
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.about_me),
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = it.value, fontSize = 16.sp)
                        }

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (phoneNumberCard != null || emailCard != null || addressCard != null) {
                    Card(
                        modifier = Modifier
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.contact_details),
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.labelLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            phoneNumberCard?.let {
                                ContactInfoRow(
                                    icon = Icons.Filled.Call,
                                    text = it.value
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            emailCard?.let {
                                ContactInfoRow(
                                    icon = Icons.Filled.Email,
                                    text = it.value
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            addressCard?.let {
                                ContactInfoRow(
                                    icon = Icons.Filled.LocationOn,
                                    text = "Pohjoisesplanadi 31, 00100 Helsinki, Finland"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                UserCardsList(restOfTheCards, withLazyColumn = false)
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
fun ScreenTitle(canEdit: Boolean, onEditClick: (() -> Unit)?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = stringResource(R.string.profile),
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier

        )
        Row {
            if (canEdit) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .fillMaxSize()
                            .clickable {
                                onEditClick?.let { it() }
                            }
                    )

                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val user =
        remember { mutableStateOf(User(UUID.randomUUID(), "Sebubebu", "shayne@example.com", "UX/UI Designer", null)) }
    val cards = remember {
        mutableListOf(
            Card(
                UUID.randomUUID(),
                user.value.id,
                "About Me",
                "Sebubebu is a results-driven Marketing Manager with a passion " +
                        "for leveraging innovative strategies to drive brand growth and " +
                        "customer engagement in the ever-evolving digital landscape." +
                        "Feel free to reach me out for any specific queries.",
                "facebook"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Title",
                "UI/UX Designer and Graphic Designer",
                "title"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Phone Number",
                "+358245304934",
                "phone"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Email",
                "email@email.com",
                "email"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Location",
                "Pohjoisesplanadi 31, 00100 Helsinki, Finland",
                "location"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Facebook",
                "https://facebook.com/something",
                "facebook"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Instagram",
                "https://instagram.com/something",
                "instagram"
            ),
        )
    }
    UserProfileScreen(
        user.value,
        cards,
        canEdit = true,
        onEditClick = {}
    )
}

