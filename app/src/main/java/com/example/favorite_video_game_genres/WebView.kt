package com.example.favorite_video_game_genres


import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//import androidx.compose.material.ScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavController


@SuppressLint("SuspiciousIndentation")
@Composable
    fun WebViewPage(navController: NavController) {
        var webViewContent: WebView? = null
            webViewContent?.loadUrl("https://raw.githubusercontent.com/amirza4/Favorite_video_game_genres/Offline_Support/README.md")

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






