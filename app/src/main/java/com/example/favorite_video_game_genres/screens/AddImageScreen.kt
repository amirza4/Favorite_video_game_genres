package com.example.favorite_video_game_genres.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class AddImageScreen {
    @SuppressLint("NotConstructor")
    @Composable
    fun AddImageScreen(dataManip: DataManipulation, navController: NavController)
    {
        BackHandler() {
            dataManip.fetchFromFireBase()
            {
                navController.navigate("DisplayScreen")
            }
        }

        var permissionGranted by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(0) }
        var askPermissions by remember { mutableStateOf(false) }

        runBlocking {
            permissionGranted = dataManip.getCameraPermission() ?: false // grab permission state
        }

        Box(Modifier.fillMaxSize().background(dataManip.primaryColor))

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                start = LocalConfiguration.current.screenWidthDp.dp *.06f,
                end = LocalConfiguration.current.screenWidthDp.dp *.06f,
                top = LocalConfiguration.current.screenHeightDp.dp *.33f,
                bottom = LocalConfiguration.current.screenHeightDp.dp *.34f
            )
            .border(
                width = 8.dp,
                color = dataManip.tertiaryColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(dataManip.secondaryColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = LocalConfiguration.current.screenWidthDp.dp *.1f,
                    end = LocalConfiguration.current.screenWidthDp.dp *.1f,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(
                "On this page you can add your favorite video game picture and upload it onto this app!",
                style = TextStyle(fontSize = 25.sp),
                color = dataManip.textLDModeColor,
                modifier = Modifier
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )
            Button(onClick = {
                if (permissionGranted) {
                    navController.navigate("CameraScreen")
                    {
                        popUpTo("AddImageScreen")
                        {
                            inclusive = true
                        }
                    }
                } else {
                    askPermissions = true // if permission wasnt set to allow
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = dataManip.tertiaryColor))
            {
                Text("Add Image", color = dataManip.textLDModeColor)
            }
        }


        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                dataManip.askingPermission = false
                permissionGranted = isGranted // set result from permission popup
                if(!permissionGranted)
                {
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(dataManip.activity, Manifest.permission.CAMERA)) //if dont allow is clicked or popup refuses to show up anymore
                    {
                        showDialog = 2
                    }
                    else // if tapped outside or no option is clicked
                    {
                        showDialog = 1
                    }
                }
                else
                {
                    runBlocking { dataManip.updateCameraPermission(permissionGranted) }
                    navController.navigate("CameraScreen") // if allowed
                }
                askPermissions = false
            }
        )

        if(askPermissions)
        {
            dataManip.askingPermission = true
            permissionLauncher.launch(Manifest.permission.CAMERA) // then request it
        }

        if(showDialog == 1)
        {
            AlertDialog(
                onDismissRequest = { },
                containerColor = dataManip.bgColor,
                title = { Text("Camera Permission Request", textAlign = TextAlign.Center, color = dataManip.textLDModeColor) },
                text = { Text("Please grant permission to access your camera to continue.", fontSize = 18.sp, color = dataManip.textLDModeColor) }, //dialog for allowing
                confirmButton = {
                    Button(onClick = {
                        askPermissions = true
                        showDialog = 0
                    }) {
                        Text(text = "Allow")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = 0
                        askPermissions = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
        else if(showDialog == 2)
        {
            AlertDialog(
                onDismissRequest = { },
                containerColor = dataManip.bgColor,
                title = { Text("Access Denied.", textAlign = TextAlign.Center, color = dataManip.textLDModeColor) }, // dialog for sending to settings since dont allow is selected
                text = { Text("You have set the permission to not allowed. If you would still like to continue, please click the go to settings button and adjust it manually.", fontSize = 18.sp, color = dataManip.textLDModeColor) },
                confirmButton = {
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.fromParts("package", dataManip.context.packageName, null)
                        dataManip.context.startActivity(intent)
                        showDialog = 0
                    }) {
                        Text(text = "Go To Settings")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = 0
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}