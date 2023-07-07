package com.example.favorite_video_game_genres

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text

class WebView {
    @Composable
    fun WebViewPage(mainActivity: MainActivity) {
        var webViewContent: WebView? = null

        Button(onClick = {
            webViewContent?.loadUrl("https://its.ny.gov/")
        }) {
            Text("Open Web View")
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewContent = this
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                }
            },
            update = {}
        )
    }
}






