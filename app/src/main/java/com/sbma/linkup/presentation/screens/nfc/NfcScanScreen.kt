package com.sbma.linkup.presentation.screens.nfc

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.nfc.NFCViewModel
import com.sbma.linkup.presentation.ui.theme.YellowApp
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NfcScanScreen(
    shareId: String,
    nfcViewModel: NFCViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClick: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_nfc))
    val toastMessage = nfcViewModel.observeToast().collectAsState(initial = null)
    val tagInfo = nfcViewModel.observeTag().collectAsState(initial = null)
    val assignTagResponseStatus = userViewModel.assignNfcStatus().collectAsState(initial = null)

    LaunchedEffect(true) {
        nfcViewModel.onCheckNFC(true)
        nfcViewModel.observeTag().collectLatest {
            it?.let {
                userViewModel.assignTag(it, shareId)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            Icon(
                Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBackClick() }
            )
        }

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.hover_nfc_card),
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(50.dp))
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .border(10.dp, YellowApp, RoundedCornerShape(70.dp))
                .clip(RoundedCornerShape(70.dp))
        )
        Spacer(modifier = Modifier.height(50.dp))
//        toastMessage.value?.let {
//            Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
//        }
        assignTagResponseStatus.value?.let {
            Text(it)
        }
    }
}