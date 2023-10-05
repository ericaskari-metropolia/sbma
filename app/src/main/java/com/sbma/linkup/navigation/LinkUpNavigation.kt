package com.sbma.linkup.navigation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.sbma.linkup.api.apimodels.ApiUser
import com.sbma.linkup.api.apimodels.toUser
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.intents.login.LoginResponseToken
import com.sbma.linkup.presentation.screens.LoadingScreen
import com.sbma.linkup.presentation.screens.LoginScreen
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationView(
    intent: Intent,
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController = rememberNavController()
    val loggedInUser = userViewModel.getLoggedInUserProfile.collectAsState(initial = null)

    // show loading screen at first if there is an intent
    val isLoading = remember { mutableStateOf(intent.action != null && intent.data != null) }

    LaunchedEffect(true) {
        // Check if there is an intent action
        intent.action?.let { action ->
            // Check if there is an intent data
            intent.data?.let { data ->

                val path = data.path ?: ""

                println("intent.action: $action")
                println("intent.data: $data")
                println("intent.path: $path")

                // Check if it is a login intent
                if (path == "/android/auth/login") {
                    val tokenQuery = data.getQueryParameter("token")
                    val userQuery = data.getQueryParameter("user")
                    val token = Gson().fromJson(tokenQuery, LoginResponseToken::class.java)
                    val apiUser = Gson().fromJson(userQuery, ApiUser::class.java)
                    val user = apiUser.toUser()
                    userViewModel.insertItem(user)
                    userViewModel.saveLoginData(
                        accessToken = token.accessToken,
                        expiresAt = token.expiresAt,
                        userId = user.id
                    )
                    delay(1000)
                }

                isLoading.value = false
            }
        }
    }

    if (loggedInUser.value == null || isLoading.value) {
        LoadingScreen()
    } else if (loggedInUser.value!!.isEmpty()) {
        LoginScreen()
    } else {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            NavigationGraph(navController, it, loggedInUser.value!!.first())
        }
    }
}
