package com.sbma.linkup

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sbma.linkup.application.MyApplication
import com.sbma.linkup.navigation.AnimatedNavigationBar
import com.sbma.linkup.navigation.BottomNavigationItemsData
import com.sbma.linkup.navigation.Height
import com.sbma.linkup.navigation.NavigationView
import com.sbma.linkup.navigation.Parabolic
import com.sbma.linkup.navigation.shapeCornerRadius
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import com.sbma.linkup.presentation.ui.theme.YellowApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver((application as MyApplication).initAppNfcManager(this))

        setContent {
            val systemUiController: SystemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.isNavigationBarVisible = false
            }
            LinkUpTheme {
                NavigationView(intent = intent)
//                DropletButtonNavBar()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun DropletButtonNavBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavigationItemsData.Share,
        BottomNavigationItemsData.MyContacts,
        BottomNavigationItemsData.Profile,
        BottomNavigationItemsData.Receive,
        BottomNavigationItemsData.Settings,
    )
    AnimatedNavigationBar(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .height(85.dp),
        selectedIndex = selectedItem,
        ballColor = YellowApp,
        cornerRadius = shapeCornerRadius(25.dp),
        ballAnimation = Parabolic(tween(Duration, easing = LinearOutSlowInEasing)),
        indentAnimation = Height(
            indentWidth = 56.dp,
            indentHeight = 15.dp,
            animationSpec = tween(
                DoubleDuration,
                easing = { OvershootInterpolator().getInterpolation(it) })
        )
    ) {
        items.forEachIndexed { index, item ->
            DropletButton(
                modifier = Modifier
                    .fillMaxSize(),
                isSelected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.screen_route)
                },
                icon = item.icon,
                dropletColor = YellowApp,
                animationSpec = tween(durationMillis = Duration, easing = LinearEasing)
            )
        }
    }
}

const val Duration = 500
const val DoubleDuration = 1000

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LinkUpTheme {
        Greeting("Android")
    }
}
