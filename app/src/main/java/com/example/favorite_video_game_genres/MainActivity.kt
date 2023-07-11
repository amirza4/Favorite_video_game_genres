package com.example.favorite_video_game_genres

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.compose.ui.unit.*
import androidx.core.view.WindowCompat
import androidx.navigation.compose.*

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

            NavHost(navController, startDestination = "CameraScreen") { //Navigate to different screens
                composable("Loading") {popups.Loading(dataManip, navController)}
                composable("DisplayScreen") {
                    scaffoldBar.ScaffoldBar(dataManip, navController) {
                        displayScreen.DisplayScreen(dataManip, navController)
                        overlay.Overlay(dataManip)
                    }
                }
                composable("InputScreen") {
                    scaffoldBar.ScaffoldBar(dataManip, navController) {
                        inputScreen.InputScreen(dataManip, navController)
                        inputScreen.CameraScreenButton(dataManip, navController)
                        overlay.Overlay(dataManip)
                    }
                }
                composable("CameraScreen") {
                    cameraScreen.CameraScreen(dataManip, navController)
                }
            }
        }
    }
}