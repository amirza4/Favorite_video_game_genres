package com.example.favorite_video_game_genres

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.core.view.WindowCompat
import androidx.navigation.compose.*


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
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
//                }
//                composable("CameraScreen") {
//                    cameraScreen.CameraScreen(dataManip, navController)
//                }
            }
            var barColor: Color

            if (dataManip.LDmode == "Light") {
                barColor = Color.White
            } else {
                barColor = Color.Black
            }
            Box {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.height(40.dp), // Height of AppBar
                            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = barColor),
                            title = {
                                Text(
                                    "Favorite Video Game Genre",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    color = dataManip.textLDModeColor
                                )  // Change Text style for the title
                            },
                            navigationIcon = {

                                if (navController.currentBackStackEntry?.destination?.route != "Loading") {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            tint = dataManip.textLDModeColor
                                        )
                                    }
                                } else{
                                    navController.navigate("InputScreen")
                                }
                            },
                            actions = {
                                IconButton(onClick = { navController.navigate("WebView") }) {
                                    Icon(
                                        Icons.Default.ArrowForward,
                                        contentDescription = "How-To-Guide",
                                        tint = dataManip.textLDModeColor
                                    )
                                }
                            }
                        )
                        //Spacer needs to be added below the TopAppBar to allow a white space separating the graph from the scaffold
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                ) {
                    NavHost(navController, startDestination = "Loading") {
                        composable("Loading") { popups.Loading(dataManip, navController) }
                        composable("DisplayScreen") {
                            displayScreen.DisplayScreen(
                                dataManip,
                                navController
                            )
                        }
                        composable("InputScreen") {
                            inputScreen.InputScreen(
                                dataManip,
                                navController
                            )
                        }
                        composable("WebView") {
                            webViewPage()
                        }
                    }
                }
                overlay.Overlay(dataManip)
            }
        }
    }
}
