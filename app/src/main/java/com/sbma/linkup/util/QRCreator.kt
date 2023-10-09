package com.sbma.linkup.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.sbma.linkup.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val MYAPI = "https://sbma.ericaskari.com/android/qr/scan?id="
@Composable
fun QRCode(data:String ) {
    val painter = rememberQrBitmapPainter(content = data)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_qr))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(-2.dp))
        Text(
            text = stringResource(R.string.scan_and_add_to_contacts),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        // Spacer to create space between text and Image
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier= Modifier.height(12.dp))

        LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                 modifier = Modifier.size(250.dp)
        )
    }
}

    @Composable
    fun MyQrCode(shareId: String, onBackClick: () -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(top = 10.dp, start = 16.dp)
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onBackClick() }
                )
            }
            QRCode( MYAPI + shareId)
        }
    }
    @Composable
    fun rememberQrBitmapPainter(
        content: String,
        size: Dp = 150.dp,
        padding: Dp = 0.dp
    ): BitmapPainter {

        val density = LocalDensity.current
        val sizePx = with(density) { size.roundToPx() }
        val paddingPx = with(density) { padding.roundToPx() }

        var bitmap by remember(content) {
            mutableStateOf<Bitmap?>(null)
        }

        LaunchedEffect(bitmap) {
            if (bitmap != null) return@LaunchedEffect

            launch(Dispatchers.IO) {
                val qrCodeWriter = QRCodeWriter()

                val encodeHints = mutableMapOf<EncodeHintType, Any?>()
                    .apply {
                        this[EncodeHintType.MARGIN] = paddingPx
                    }

                val bitmapMatrix = try {
                    qrCodeWriter.encode(
                        content, BarcodeFormat.QR_CODE,
                        sizePx, sizePx, encodeHints
                    )
                } catch (ex: WriterException) {
                    null
                }

                val matrixWidth = bitmapMatrix?.width ?: sizePx
                val matrixHeight = bitmapMatrix?.height ?: sizePx

                val newBitmap = Bitmap.createBitmap(
                    bitmapMatrix?.width ?: sizePx,
                    bitmapMatrix?.height ?: sizePx,
                    Bitmap.Config.ARGB_8888,
                )

                for (x in 0 until matrixWidth) {
                    for (y in 0 until matrixHeight) {
                        val shouldColorPixel = bitmapMatrix?.get(x, y) ?: false
                        val pixelColor = if (shouldColorPixel) Color.BLACK else Color.WHITE

                        newBitmap.setPixel(x, y, pixelColor)
                    }
                }

                bitmap = newBitmap
            }
        }

        return remember(bitmap) {
            val currentBitmap = bitmap ?: Bitmap.createBitmap(
                sizePx, sizePx,
                Bitmap.Config.ARGB_8888,
            ).apply { eraseColor(Color.TRANSPARENT) }

            BitmapPainter(currentBitmap.asImageBitmap())
        }
    }
