package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R

@Composable
fun ScanResultScreen (){

    val successQrScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_qr))
    val failedQrScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.failed_qr_scan))
    val successNFCScan by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_qr))

    Column {
        Text(text="You have been successfully added to my contact List.")
        Spacer(modifier= Modifier.height(12.dp))

        LottieAnimation(
            composition = successQrScan,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(250.dp)
        )
    }

}