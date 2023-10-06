package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
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
fun UserShareScreenProvider(user: User, onSuccessResponse: (shareId: String) -> Unit) {
    val userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userCards = userCardViewModel.allItemsStream(user.id).collectAsState(initial = null)
    val composableScope = rememberCoroutineScope()

    userCards.value?.let {
        UserShareScreen(it, onShareClick = { cardsToShare ->
            composableScope.launch {
                userViewModel.shareCards(cardsToShare.map { card -> card.id.toString() }, onSuccessResponse)
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserShareScreenTopBar(scrollBehavior: TopAppBarScrollBehavior, enableNext: Boolean, onNextClick: () -> Unit) {

    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "What would you like to share?",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(
                modifier = Modifier,
                onClick = { onNextClick() },
                enabled = enableNext
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserShareScreen(userCards: List<Card>, onShareClick: (userCards: List<Card>) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val input = rememberSaveable {
        mutableStateOf(userCards.map { Pair(it, false) })
    }
    Scaffold(
        topBar = {
            UserShareScreenTopBar(scrollBehavior, input.value.any { it.second }) {
                val selectedItems = input.value.filter { pair -> pair.second }
                val selectedCards = selectedItems.map { it.first }
                onShareClick(selectedCards)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            itemsIndexed(input.value) { index, mediaItem ->
                InfoListItem(mediaItem) {
                    val copy = input.value.toMutableList()
                    val newEl = mediaItem.copy(second = !mediaItem.second)
                    copy[index] = newEl
                    input.value = copy
                }
            }
        }
    }
}


@Composable
fun InfoListItem(item: Pair<Card, Boolean>, onItemClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onItemClick),
        headlineContent = {
            Text(text = item.first.title, style = MaterialTheme.typography.bodyLarge)
        },
        trailingContent = {
            Switch(
                checked = item.second,
                onCheckedChange = { onItemClick() }
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun UserShareScreenPreview() {
    val userId = UUID.randomUUID()
    val cards = remember {
        mutableListOf(
            Card(UUID.randomUUID(), userId, "Facebook", "facebook.com/something", null),
            Card(UUID.randomUUID(), userId, "Instagram", "instagram.com/something", null),
            Card(UUID.randomUUID(), userId, "Twitter", "twitter.com/something", null)
        )
    }
    LinkUpTheme {
        UserShareScreen(cards) {

        }
    }
}