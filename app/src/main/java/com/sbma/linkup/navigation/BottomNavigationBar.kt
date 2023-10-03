package com.sbma.linkup.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Share,
        BottomNavItem.MyContacts,
        BottomNavItem.Profile,
        BottomNavItem.Receive,
        BottomNavItem.Setting
    )

    BottomAppBar(
        modifier = Modifier
            .graphicsLayer {
                shape = RoundedCornerShape(
                    topStart = 25.dp,
                    topEnd = 25.dp,
                )
                clip = true
            },
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 15.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, // evenly space icons
            modifier = Modifier.fillMaxWidth(),
        ) {
            var selectedItem by remember { mutableStateOf(0) }
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == index
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        selectedItem = index
                        navController.navigate(item.screen_route) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(35.dp)
                            .padding(bottom = 4.dp),
                        tint = if (isSelected) Color.Yellow else Color.Black
                    )
                    Text(
                        text = item.title,
                        fontSize = 9.sp,
                        color = if (isSelected) Color.Yellow else Color.Black
                    )
                }
            }
        }
    }
}


