package com.sbma.linkup.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R
import com.sbma.linkup.presentation.components.SignInGoogleButton
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val composableScope = rememberCoroutineScope()
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://sbma.ericaskari.com/auth/google")) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_ln6i2ywv))
    val progress by animateLottieCompositionAsState(composition)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Text(text = "LOGIN")
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
//            modifier = Modifier.border(10.dp, YellowApp)
//                .clip(RoundedCornerShape(70.dp))



        )
        SignInGoogleButton {
            context.startActivity(intent)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LinkUpTheme {
        Surface {
            LoginScreen()
        }
    }
}
