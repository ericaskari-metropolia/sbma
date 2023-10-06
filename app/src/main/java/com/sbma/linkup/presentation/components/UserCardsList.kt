package com.sbma.linkup.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbma.linkup.R
import com.sbma.linkup.card.Card
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import java.util.UUID

@Composable
fun UserCardsListItem(userCard: Card) {
    val size = remember {
        mutableStateOf(50.dp)
    }
    ListItem(
        overlineContent = { Text(userCard.title) },
        headlineContent = { Text(userCard.value) },

        leadingContent = {
            when (userCard.title) {
                "Facebook" -> Image(
                    painterResource(R.drawable.facebook),
                    modifier = Modifier.width(size.value).height(size.value),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                )

                "Instagram" -> Image(
                    painterResource(R.drawable.instagram),
                    modifier = Modifier.width(size.value).height(size.value),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                )

                "Linkedin" -> Image(
                    painterResource(R.drawable.linkedin),
                    modifier = Modifier.width(size.value).height(size.value),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                )

                "Twitter" -> Image(
                    painterResource(R.drawable.twitter),
                    modifier = Modifier.width(size.value).height(size.value),
                    contentDescription = "Edit",
                    contentScale = ContentScale.Crop,
                )

                else -> Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "AccountCircle",
                    )
                }
            }
        }
    )
}

@Composable
fun UserCardsList(userCards: List<Card>, withLazyColumn: Boolean = true, modifier: Modifier = Modifier) {
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
            Card(UUID.randomUUID(), UUID.randomUUID(), "Facebook", "https://facebook.com/something", null),
            Card(UUID.randomUUID(), UUID.randomUUID(), "Instagram", "https://instagram.com/something", null),
            Card(UUID.randomUUID(), UUID.randomUUID(), "Twitter", "https://twitter.com/something", null),
            Card(UUID.randomUUID(), UUID.randomUUID(), "Twitch", "https://twitch.com/something", null),
        )
    }
    LinkUpTheme {
        Scaffold {
            UserCardsList(cards)
        }
    }
}