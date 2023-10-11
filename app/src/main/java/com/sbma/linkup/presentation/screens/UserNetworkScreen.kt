package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.Connection
import com.sbma.linkup.presentation.screenstates.UserConnectionsScreenState
import com.sbma.linkup.presentation.ui.theme.LightGray
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.presentation.ui.theme.PurpleGrey80
import com.sbma.linkup.presentation.ui.theme.YellowApp
import com.sbma.linkup.user.User
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNetworkScreenTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.my_contacts),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun UserNetworkListItem(
    user: User,
    connection: Connection,
    onConnectionClick: (connection: Connection) -> Unit,
    userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val userCards by
    userCardViewModel.allItemsStream(user.id).collectAsState(initial = listOf())
    val titleCard = userCards.find { it.title == "Title" }

    ListItem(
        modifier = Modifier.clickable(
            onClick = {
                onConnectionClick(connection)
            }
        ),
        headlineContent = { Text(user.name) },
        supportingContent = { Text(titleCard?.value ?: "") },
        leadingContent = {
            AsyncImage(
                model = user.picture,
                contentScale = ContentScale.Crop,
                contentDescription = "User picture",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .size(48.dp),
            )
        }
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp),
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNetworkScreen(
    state: UserConnectionsScreenState,
    modifier: Modifier = Modifier,
    onConnectionClick: (connection: Connection) -> Unit
) {
    val contacts = state.connections.entries.toList()
    var searchQuery by remember { mutableStateOf("") }

    val filteredContacts = if (searchQuery.isEmpty()) {
        contacts
    } else {
        contacts.filter { contact ->
            contact.value.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            UserNetworkScreenTopBar()
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 110.dp),
            content = {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newValue ->
                        searchQuery = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(
                            color = PurpleGrey80,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    placeholder = { Text(text = "Search Contacts") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                LazyColumn(
                    modifier
                        .padding(16.dp)
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    items(filteredContacts) { contact ->
                        UserNetworkListItem(
                            user = contact.value,
                            connection = contact.key,
                            onConnectionClick = onConnectionClick
                        )
                    }
                }
            })
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    val userId by remember {
        mutableStateOf(UUID.randomUUID())
    }

    val users: Map<Connection, User> = remember {
        mapOf(
            Connection(
                id = UUID.randomUUID(),
                userId = userId,
                connectedUserId = UUID.randomUUID()
            ) to User(
                id = UUID.randomUUID(),
                name = "Eric",
                description = "Mobile developer",
                email = "",
                picture = null
            ),

            Connection(
                id = UUID.randomUUID(),
                userId = userId,
                connectedUserId = UUID.randomUUID()
            ) to User(
                id = UUID.randomUUID(),
                name = "Shayne",
                description = "Mobile developer",
                email = "",
                picture = null

            ),

            Connection(
                id = UUID.randomUUID(),
                userId = userId,
                connectedUserId = UUID.randomUUID()
            ) to User(
                id = UUID.randomUUID(),
                name = "Binod",
                description = "Mobile developer",
                email = "",
                picture = null
            ),

            Connection(
                id = UUID.randomUUID(),
                userId = userId,
                connectedUserId = UUID.randomUUID()
            ) to User(
                id = UUID.randomUUID(),
                name = "Sebastian",
                description = "Mobile developer",
                email = "",
                picture = null
            )
        )
    }
    LinkUpTheme {
        UserNetworkScreen(
            UserConnectionsScreenState(users)
        ) {
            println(it)
        }
    }
}
