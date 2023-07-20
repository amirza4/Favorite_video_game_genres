package com.example.favorite_video_game_genres.accessories

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation
import com.example.favorite_video_game_genres.ui.theme.DarkColorScheme
import com.example.favorite_video_game_genres.ui.theme.LightColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Popups {

    private fun Navigate(dataManip: DataManipulation, navController: NavController)
    {
        dataManip.fetchFromFireBase()//Grab data from firebase, then navigate to screen
        {
            navController.navigate("DisplayScreen")
            {
                popUpTo("Loading") {
                    inclusive = true
                }
            }
        }
    }
    @Composable
    fun Loading(dataManip: DataManipulation, navController: NavController) {//Do Loading Screen while waiting for data
        var label by remember { mutableStateOf("") }
        LaunchedEffect(Unit)
        {
            if (dataManip.getLDMode() == true) {
                CoroutineScope(Dispatchers.IO).launch { dataManip.updateLDMode(true) }
                dataManip.LDmode = "Dark"
                dataManip.textLDModeColor = Color.White
                dataManip.bgColor = Color.Black
                dataManip.primaryColor = DarkColorScheme.primary
                dataManip.secondaryColor = DarkColorScheme.secondary
                dataManip.tertiaryColor = DarkColorScheme.tertiary
            } else {
                CoroutineScope(Dispatchers.IO).launch { dataManip.updateLDMode(false) }
                dataManip.LDmode = "Light"
                dataManip.textLDModeColor = Color.Black
                dataManip.bgColor = Color.White
                dataManip.primaryColor = LightColorScheme.primary
                dataManip.secondaryColor = LightColorScheme.secondary
                dataManip.tertiaryColor = LightColorScheme.tertiary
            }

            val votesList = dataManip.getCache()
            val connectivityManager = dataManip.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager.activeNetwork != null)
            {
                Navigate(dataManip, navController)
                label = "Loading...."
            } else
            {
                if (votesList.isNullOrEmpty())  //Checks and safety flow
                {
                    label = "No internet connection\nor data in cache."
                    delay(5000)
                    dataManip.activity.finishAffinity()
                } else
                {
                    label = "Pulling from cache...."
                    Navigate(dataManip, navController)
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