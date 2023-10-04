package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.presentation.screenstates.UserConnectionsScreenState
import com.sbma.linkup.ui.theme.LinkUpTheme
import com.sbma.linkup.user.User
import java.util.UUID

@Composable
fun UserConnectionsScreenProvider(
    user: User,
) {
    val userConnectionViewModel: ConnectionViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userItems = userConnectionViewModel
        .allItemsStream(user.id)
        .collectAsState(initial = mapOf())

    val state = UserConnectionsScreenState(
        contacts = userItems.value.values.toList()
    )

    if (state.contacts.isEmpty()) {
        EmptyUserConnectionsScreen()
    } else {
        UserConnectionsScreen(state) {
            // TODO: What happens when clicking on list item.
        }
    }
}

@Composable
fun EmptyUserConnectionsScreen() {
    Text(text = "Contact list is empty!")
}

@Composable
fun UserConnectionsScreen(
    state: UserConnectionsScreenState,
    modifier: Modifier = Modifier,
    onItemClick: (user: User) -> Unit
) {

    LazyColumn(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        items(state.contacts) { contact ->
            ListItem(
                modifier = Modifier.clickable(
                    onClick = {
                        onItemClick(contact)
                    }
                ),
                headlineContent = { Text(contact.name) },
                supportingContent = {
                    Column {
                        Text(contact.description)
                        Row {
                            Icon(
                                Icons.Filled.Phone,
                                contentDescription = "Localized description",
                            )
                            Text("+358 1234567890")
                        }
                    }
                },
                leadingContent = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                }
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    LinkUpTheme {
        UserConnectionsScreen(
            UserConnectionsScreenState(
                listOf(
                    User(
                        id = UUID.randomUUID(),
                        name = "Eric",
                        description = "Mobile developer",
                        email = "",
                        picture = null
                    ),
                    User(
                        id = UUID.randomUUID(),
                        name = "Shayne",
                        description = "Mobile developer",
                        email = "",
                        picture = null

                    ),
                    User(
                        id = UUID.randomUUID(),
                        name = "Binod",
                        description = "Mobile developer",
                        email = "",
                        picture = null
                    ),
                    User(
                        id = UUID.randomUUID(),
                        name = "Sebastian",
                        description = "Mobile developer",
                        email = "",
                        picture = null
                    )
                )
            )
        ) {
            println(it)
        }
    }
}
