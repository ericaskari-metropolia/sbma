package com.sbma.linkup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sbma.linkup.presentation.screens.UserConnectionsScreenProvider
import com.sbma.linkup.navigation.NavigationView
import com.sbma.linkup.ui.theme.LinkUpTheme
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()

            LinkUpTheme {

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { backStackEntry ->
                        // A surface container using the 'background' color from the theme
                        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                            Greeting("Android")
                        }
                    }
                    composable(
                        "users/{userId}/contacts",
                        arguments = listOf(navArgument("userId") { type = NavType.StringType }),
                    ) { backStackEntry ->
                        backStackEntry.arguments?.getString("userId")?.let {
                            UserConnectionsScreenProvider(
                                userId = UUID.fromString(it))
                        }

                    }

                // A surface container using the 'background' color from the theme
                //Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                 //   NavigationView()

                }


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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LinkUpTheme {
        Greeting("Android")
    }
}