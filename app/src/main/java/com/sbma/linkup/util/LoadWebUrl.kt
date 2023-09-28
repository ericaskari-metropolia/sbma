package com.sbma.linkup.util

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

//** TO DO -> More efficient scanning of the QR code ** //

//This function loads the url based on qr code
@Composable
fun LoadWebUrl(url: String) {
    AndroidView(factory = {
        WebView(it).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}