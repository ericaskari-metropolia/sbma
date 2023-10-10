package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val composableScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = stringResource(R.string.setting_screen),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Button(onClick = {
            composableScope.launch {
                userViewModel.deleteLoginData()
            }
        }) {
            Text(text = stringResource(R.string.logout))
        }
    }
}
