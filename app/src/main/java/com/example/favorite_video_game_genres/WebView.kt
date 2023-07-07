package com.example.favorite_video_game_genres


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

class WebView : Fragment() {
    @Composable
    fun WebViewPage(mainActivity: MainActivity, navController: NavController) {
        var webViewContent: WebView? = null

        Column() {
            Button(onClick = {
                webViewContent?.loadUrl("https://its.ny.gov/")
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 60.dp, end = 60.dp, bottom = 10.dp)
            )

            {
                Text("Open Web View")
            }
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






