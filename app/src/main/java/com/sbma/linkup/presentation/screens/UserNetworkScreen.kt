package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.Connection
import com.sbma.linkup.presentation.screenstates.UserConnectionsScreenState
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
fun UserNetworkScreen(
    state: UserConnectionsScreenState,
    modifier: Modifier = Modifier,
    onConnectionClick: (connection: Connection) -> Unit
) {

    var searchValue by rememberSaveable { mutableStateOf("") }


    Scaffold(
        topBar = {
            Column {
                UserNetworkScreenTopBar()
                ContactSearch(
                    onValueChange = {
                        searchValue = it
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier
                .padding(padding)
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(state.connections.entries.toList().filter {
                it.value.name.contains(searchValue, ignoreCase = true)
            }) { contact ->
                UserNetworkListItem(
                    user = contact.value,
                    connection = contact.key,
                    onConnectionClick = onConnectionClick
                )
            }
        }
    }
}

@Composable
fun ContactSearch(onValueChange:(String) -> Unit){
    var searchText by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }

    if (searchText.isEmpty()) {
        showClearIcon = false
    } else if (searchText.isNotEmpty()) {
        showClearIcon = true
    }


    TextField(
        value = searchText,
        onValueChange = { searchText = it
                        onValueChange(it)},
        placeholder = { Text("Search for Connections") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "searchIcon") },
        trailingIcon = {
            if (showClearIcon) {
                IconButton(onClick = { searchText = "" }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Clear Icon"
                    )
                }
            }
        },
        modifier = Modifier
            .padding(25.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = RectangleShape)
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
