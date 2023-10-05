package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.connection.Connection
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.presentation.screenstates.UserConnectionsScreenState
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.user.User
import java.util.UUID

@Composable
fun UserConnectionsScreenProvider(
    user: User,
    onConnectionClick: (connection: Connection) -> Unit
) {
    val userConnectionViewModel: ConnectionViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userItems = userConnectionViewModel
        .allItemsStream(user.id)
        .collectAsState(initial = mapOf())

    val state = UserConnectionsScreenState(
        connections = userItems.value
    )

    if (state.connections.values.isEmpty()) {
        EmptyUserConnectionsScreen()
    } else {
        UserConnectionsScreen(state) {
            onConnectionClick(it)
        }
    }
}

@Composable
fun EmptyUserConnectionsScreen() {
    Text(text = "Contact list is empty!")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserConnectionsScreenTopBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                "My Contacts",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        scrollBehavior = scrollBehavior
    )
}
@Composable
fun UserConnectionsScreen(
    state: UserConnectionsScreenState,
    modifier: Modifier = Modifier,
    onConnectionClick: (connection: Connection) -> Unit
) {

    Scaffold(
        topBar = {
            UserConnectionsScreenTopBar()
        }
    ) { padding ->
        LazyColumn(
            modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(state.connections.entries.toList()) { contact ->
                ListItem(
                    modifier = Modifier.clickable(
                        onClick = {
                            onConnectionClick(contact.key)
                        }
                    ),
                    headlineContent = { Text(contact.value.name) },
                    leadingContent = {
                        AsyncImage(
                            model = contact.value.picture,
                            contentScale = ContentScale.Crop,
                            contentDescription = "User picture",
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                )


            }
        }
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
        UserConnectionsScreen(
            UserConnectionsScreenState(users)
        ) {
            println(it)
        }
    }
}
