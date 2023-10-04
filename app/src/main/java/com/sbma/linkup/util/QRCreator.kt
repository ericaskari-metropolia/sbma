package com.sbma.linkup.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.user.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val MYAPI = "https://sbma.ericaskari.com/android/qr/scan?id="
@Composable
fun QRCode(data:String, ) {
    var painter = rememberQrBitmapPainter(content = data)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}

    @Composable
    fun MyQrCode() {
        var userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

        val getQRString = userViewModel.shareId.collectAsState(initial = null)
        var qrCodeContent by remember { mutableStateOf("") }

        var isVisible by remember { mutableStateOf(false) }


        Column(modifier = Modifier.fillMaxWidth()) {
            getQRString.value?.let{myData->
                QRCode( MYAPI + myData)

            }
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
