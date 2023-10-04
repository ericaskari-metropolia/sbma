package com.sbma.linkup.presentation.screens.bluetooth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.card.CardViewModel

@Composable()
fun ShareViaBluetoothScreenProvider() {
    val userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val userCards = userCardViewModel.jsonToShare.collectAsState(initial = null)
    println(userCards)
    ShareViaBluetoothScreen()
}

@Composable()
fun ShareViaBluetoothScreen() {

}