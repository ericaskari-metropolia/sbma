package com.sbma.linkup.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbma.linkup.presentation.screens.ContactInfoRow
import com.sbma.linkup.ui.theme.LinkUpTheme
import com.sbma.linkup.usercard.UserCard
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@Composable
fun UserCardsListItem(userCard: UserCard) {
    ListItem(
        overlineContent = { Text(userCard.name) },
        headlineContent = { Text(userCard.value) },
    )
}

@Composable
fun UserCardsList(userCards: List<UserCard>, withLazyColumn: Boolean = true, modifier: Modifier = Modifier) {
    if (withLazyColumn) {
        LazyColumn(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(userCards) {
                UserCardsListItem(it)
            }
        }
    } else {
        userCards.forEach {
            UserCardsListItem(it)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun UserCardsListPreview() {
    val cards = remember {
        mutableListOf(
            UserCard(UUID.randomUUID(), UUID.randomUUID(), "Facebook", "https://facebook.com/something"),
            UserCard(UUID.randomUUID(), UUID.randomUUID(), "Instagram", "https://instagram.com/something"),
        )
    }
    LinkUpTheme {
        Scaffold {
            UserCardsList(cards)
        }
    }
}