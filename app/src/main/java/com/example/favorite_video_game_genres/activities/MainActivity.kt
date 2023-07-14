package com.example.favorite_video_game_genres.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.*
import androidx.navigation.compose.*
import com.example.favorite_video_game_genres.screens.InputScreen
import com.example.favorite_video_game_genres.accessories.*
import com.example.favorite_video_game_genres.data.*
import com.example.favorite_video_game_genres.screens.*


class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataManip = DataManipulation(this, this)

        setContent {
            val navController = rememberNavController()
            val overlay = Overlay()
            val displayScreen = DisplayScreen()
            val inputScreen = InputScreen()
            val popups = Popups()
            val cameraScreen = CameraScreen()
            val scaffoldBar = Scaffold()
            val imageDisplay = ImageDisplay()
            val addImageScreen = AddImageScreen()

            NavHost(navController, startDestination = "Loading") { //Navigate to different screens
                composable("Loading") { popups.Loading(dataManip, navController) }
                composable("DisplayScreen") {
                    scaffoldBar.ScaffoldBar(dataManip, navController) {
                        displayScreen.DisplayScreen(dataManip, navController)
                        overlay.CameraScreenButton(dataManip, navController)
                        overlay.Overlay(dataManip)
                    }
                }
                composable("InputScreen") {
                    scaffoldBar.ScaffoldBar(dataManip, navController) {
                        inputScreen.InputScreen(dataManip, navController)
                        overlay.CameraScreenButton(dataManip, navController)
                        overlay.Overlay(dataManip)
                    }
                }
                composable("CameraScreen") {
                    cameraScreen.CameraScreen(dataManip, navController)
                }
                composable("ImageDisplay")
                {
                    val imageID: ImageBitmap? by remember {
                        mutableStateOf(
                            dataManip.decodeImage(
                                dataManip.returnImageFile()
                            )?.asImageBitmap()
                        )
                    }
                    if (imageID != null) {
                        scaffoldBar.ScaffoldBar(dataManip, navController) {
                            imageDisplay.ImageDisplay(dataManip, navController, imageID!!)
                            overlay.Overlay(dataManip)
                        }
                    } else {
                        navController.navigate("DisplayScreen")
                        {
                            popUpTo("AddImageScreen")
                        }
                    }
                }
                composable("AddImageScreen")
                {
                    scaffoldBar.ScaffoldBar(dataManip, navController) {
                        addImageScreen.AddImageScreen(dataManip, navController)
                        overlay.Overlay(dataManip)
                    }
                }
                composable("WebView") {
                    scaffoldBar.ScaffoldBar(dataManip, navController) {
                        webViewPage()
                    }
                }
            }
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}