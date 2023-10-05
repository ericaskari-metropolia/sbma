package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.user.User
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun UserShareScreenProvider(user: User, onShareClick: () -> Unit) {
    val userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userCards = userCardViewModel.allItemsStream(user.id).collectAsState(initial = null)
    val composableScope = rememberCoroutineScope()

    userCards.value?.let {
        UserShareScreen(it, onShareClick = {cardsToShare ->
            composableScope.launch {
                userViewModel.shareCards(it.map { card -> card.id.toString() })
                onShareClick()
            }
        })
    }
}


@Composable
fun UserShareScreen(userCards: List<Card>, onShareClick: (userCards: List<Card>) -> Unit) {
    val input = remember {
        mutableStateOf(userCards.map { Pair(it, false) })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Share",
            fontSize = 25.sp,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = "What would you like to share?",
            style = MaterialTheme.typography.labelLarge,
            fontSize = 20.sp
        )

        LazyColumn {
            itemsIndexed(input.value) { index, mediaItem ->
                InfoListItem(mediaItem) {
                    val copy = input.value.toMutableList()
                    val newEl = mediaItem.copy(second = !mediaItem.second)
                    copy[index] = newEl
                    input.value = copy
                }
            }
        }

        Button(
            onClick = {
                onShareClick(
                    input.value
                        .filter { pair -> pair.second }
                        .map { it.first }
                )
            },
            modifier = Modifier.padding(bottom = 100.dp)
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Share")
        }
    }
}


@Composable
fun InfoListItem(item: Pair<Card, Boolean>, onItemClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = onItemClick),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.first.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = item.second,
                onCheckedChange = { onItemClick() }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserShareScreenPreview() {
    val userId = UUID.randomUUID()
    val cards = remember {
        mutableListOf(
            Card(UUID.randomUUID(), userId, "Facebook", "facebook.com/something"),
            Card(UUID.randomUUID(), userId, "Instagram", "instagram.com/something"),
            Card(UUID.randomUUID(), userId, "Twitter", "twitter.com/something")
        )
    }
    LinkUpTheme {
        UserShareScreen(cards) {

        }
    }
}