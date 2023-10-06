package com.sbma.linkup.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(2) }

    val items = listOf(
        BottomNavItem.Share,
        BottomNavItem.MyContacts,
        BottomNavItem.Profile,
        BottomNavItem.Receive,
        BottomNavItem.Setting
    )

    NavigationBar(
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 25.dp,
                    topEnd = 25.dp,
                )
            },
        containerColor = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 15.dp,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(bottom = 4.dp),
                    )
                },
                label = { Text(item.title, maxLines = 1, softWrap = false) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.screen_route)
                }
            )
        }
    }
}


