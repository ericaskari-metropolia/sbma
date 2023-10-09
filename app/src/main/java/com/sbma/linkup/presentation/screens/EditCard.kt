package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbma.linkup.card.Card
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.util.toPictureResource
import java.util.UUID

@Composable
fun EditCard(
    card: Card,
    canEditTitle: Boolean,
    onCardModified: (card: Card) -> Unit,
    onDelete: (card: Card) -> Unit,
    onPictureClick: (card: Card) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(10.dp),
        content = {
            ListItem(
                headlineContent = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (canEditTitle) {
                            false -> Text(text = card.title, modifier = Modifier.weight(1f))
                            else -> TextField(
                                modifier = Modifier.weight(1f),
                                value = card.title,
                                onValueChange = {
                                    onCardModified(card.copy(title = it))
                                },
                                label = { Text("Title") },
                            )
                        }
                        IconButton(
                            modifier = Modifier,
                            onClick = { onDelete(card) },
                            enabled = true
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                },
                supportingContent = {
                    TextField(
                        value = card.value,
                        onValueChange = {onCardModified(card.copy(value = it))},
                        label = { Text("Value") },
                    )
                },
                leadingContent = {
                    Image(
                        painterResource(card.picture.toPictureResource()),
                        contentDescription = "Card Icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                            .clickable { onPictureClick(card) }
                    )
                }
            )

        }
    )

}

@Preview(showBackground = true)
@Composable
fun CreateCardPreview() {
    LinkUpTheme {
        EditCard(
            card = Card(UUID.randomUUID(), UUID.randomUUID(), "Instagram", "https://instagram.com/something", "instagram"),
            canEditTitle= true,
            onCardModified = {},
            onPictureClick = {},
            onDelete = {},
        )
    }
}
@Preview(showBackground = true)
@Composable
fun CreateCardWithoutTitlePreview() {
    LinkUpTheme {
        EditCard(
            card = Card(UUID.randomUUID(), UUID.randomUUID(), "Instagram", "https://instagram.com/something", "instagram"),
            canEditTitle= false,
            onCardModified = {},
            onPictureClick = {},
            onDelete = {},
        )
    }
}