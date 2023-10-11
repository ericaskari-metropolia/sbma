package com.sbma.linkup.presentation.screens.qr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.net.Uri
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.sbma.linkup.application.data.AppViewModelProvider
import com.sbma.linkup.presentation.ui.theme.YellowApp
import com.sbma.linkup.user.UserViewModel
import com.sbma.linkup.util.MYAPI
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

@Composable
fun ScanQRCodeCameraScreen(
    userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClick: () -> Unit,
    onResultScan: (Boolean) -> Unit
) {

    val scope = rememberCoroutineScope()
    var code by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            CameraScreenTopBar {
                onBackClick()
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(top = 20.dp),
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Place the QR code in this frame",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            QrCodeReader(
                { result ->
                    scope.launch {
                        code = result
                        println("Result: $result")
                        val id = code.split(MYAPI).last()
                        userViewModel.scanQRCode(id) {
                           onResultScan(it)
                        }

                    }

                },
                modifier = Modifier
                    .size(width = 265.dp, height = 360.dp)
                    .border(10.dp, YellowApp, RoundedCornerShape(70.dp))
                    .clip(RoundedCornerShape(70.dp))
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "$code")
            /*  if (URLUtil.isValidUrl(code)) {
                  ResultLink(code)
              } else {
                  ResultText(code)
              }*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreenTopBar(
    onBackClick: () -> Unit
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = "QR code transfer",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier,
                onClick = { onBackClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}

@Composable
private fun Header(modifier: Modifier = Modifier, onBackClicked: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBackClicked() }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Show me your QR code", modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontSize = 20.sp
        )
    }
}


@Composable
private fun ResultText(code: String, modifier: Modifier = Modifier) {
    Text(
        text = code,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    )
}

@Composable
fun ResultLink(code: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val annotatedLinkString = buildAnnotatedString {
        append(code)
        addStyle(
            style = SpanStyle(
                color = Color(0xff64B5F6),
                textDecoration = TextDecoration.Underline,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ), start = 0, end = code.length
        )
    }
    ClickableText(
        text = annotatedLinkString,
        onClick = { openUrl(code, context) },
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    )
}

private fun openUrl(url: String, ctx: Context) {

    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(ctx, webIntent, null)
}

class QrCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
    )

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormats) {
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
                onQrCodeScanned(result.text)
            } catch (e: Exception) {
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

@Composable
fun QrCodeReader(
    onSendCode: (code: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
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

        })
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    if (hasCamPermission) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 265.dp, height = 265.dp)


            ) {
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
                                Size(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer { result ->
                                onSendCode(result)
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
                    },
                    modifier = modifier
                )
            }
        }
    }
}
