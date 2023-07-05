package com.example.favorite_video_game_genres

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

class Popups {

    fun navigate(dataManip: DataManipulation, navController: NavController)
    {
        dataManip.fetchFromFireBase()
        {
            navController.navigate("DisplayScreen")
            {
                popUpTo("Loading")
            }
        }
    }
    @Composable
    fun Loading(dataManip: DataManipulation, navController: NavController) {
        var label by remember { mutableStateOf("") }
        LaunchedEffect(Unit)
        {
            val votesList = dataManip.getCache()
            val connectivityManager = dataManip.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager.activeNetwork != null)
            {
                navigate(dataManip, navController)
                label = "Loading...."
            } else
            {
                if (votesList.isNullOrEmpty())
                {
                    label = "No internet connection\nor data in cache."
                    delay(5000)
                    exitProcess(0)
                } else
                {
                    label = "Pulling from cache...."
                    navigate(dataManip, navController)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .scale(LocalConfiguration.current.screenHeightDp.toFloat() * .0025f),
            contentAlignment = Alignment.Center
        )
        {
            CircularProgressIndicator()
            Text(text = label, modifier = Modifier.offset(y = (LocalConfiguration.current.screenHeightDp.toFloat() * .06f).dp), textAlign = TextAlign.Center)
        }
    }
}