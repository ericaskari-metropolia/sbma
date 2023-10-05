package com.sbma.linkup.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer
import android.util.Size as AndroidSize

@Composable
fun CameraScreen() {
    var focusBoxPos by remember { mutableStateOf(Offset(0f, 0f)) }
    var focusBoxSiz by remember { mutableStateOf(Size(0f, 0f)) }
    var code by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasReadCode by remember {
        mutableStateOf(false)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (hasCamPermission) {
            if (hasReadCode) {
                LoadWebUrl(code)
                hasReadCode = true
            } else {
                AndroidView(
                    factory = { context ->
                        val previewView = PreviewView(context)
                        val preview = Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(
                                AndroidSize(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QRCode{ result,focusBoxPosition, focusBoxSize ->
                                code = result
                                focusBoxPos = focusBoxPosition
                                focusBoxSiz = focusBoxSize
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    }
                )

                Text(
                    text = code,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
                if (code.isNotEmpty()) {
                    DrawDynamicFocusBox(focusBoxPos, focusBoxSiz)
                }
            }
        }
    }
}



@Composable
fun LoadWebUrl(url: String) {
    val ctx = LocalContext.current
    AndroidView(factory = {
        WebView(ctx).apply {
            webViewClient = WebViewClient()
            Log.d("DBG", "checking url")
            loadUrl(url)
        }
    })
}


@Composable
fun DrawDynamicFocusBox(focusBoxPosition: Offset, focusBoxSize: androidx.compose.ui.geometry.Size) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val strokeWidth = 6f
        val color = Color.Blue


        drawRect(
            color = color,
            topLeft = focusBoxPosition,
            size = focusBoxSize,
            style = Stroke(strokeWidth)
        )
    }
}
private fun calculateQRCodePosition(result: Result, imageWidth: Int, imageHeight: Int): Offset {

    val rect = result.resultPoints ?: return Offset(0f, 0f)


    val avgX = rect.map { it.x }.average().toFloat()
    val avgY = rect.map { it.y }.average().toFloat()


    val cameraX = avgX * imageWidth
    val cameraY = avgY * imageHeight


    return Offset(cameraX, cameraY)
}

private fun calculateQRCodeSize(result: Result, imageWidth: Int, imageHeight: Int): Size {

    val rect = result.resultPoints ?: return Size(0f, 0f)


    val width = (rect[2].x - rect[0].x) * imageWidth
    val height = (rect[2].y - rect[0].y) * imageHeight


    return Size(width, height)
}


class QRCode(
    private val onQrCodeScanned: (String, Offset, Size) -> Unit
): ImageAnalysis.Analyzer {

    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    override fun analyze(image: ImageProxy) {
        if(image.format in supportedImageFormats) {
            val bytes = image.planes.first().buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )
            val binaryBmp = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(
                            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                                BarcodeFormat.QR_CODE
                            )
                        )
                    )
                }.decode(binaryBmp)

                val qrCodePosition = calculateQRCodePosition(result, image.width, image.height)
                val qrCodeSize = calculateQRCodeSize(result, image.width, image.height)
                onQrCodeScanned(result.text,qrCodePosition,qrCodeSize)
            } catch(e: Exception) {
                e.printStackTrace()
            } finally {
                image.close()
            }
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also {
            get(it)
        }
    }
}