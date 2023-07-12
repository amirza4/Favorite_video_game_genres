package com.example.favorite_video_game_genres.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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

class AddImageScreen {
    @SuppressLint("NotConstructor")
    @Composable
    fun AddImageScreen(dataManip: DataManipulation, navController: NavController)
    {
        var permissionGranted by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(0) }
        var askPermissions by remember { mutableStateOf(false) }

        runBlocking {
            permissionGranted = dataManip.getCameraPermission() ?: false // grab permission state
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(dataManip.primaryColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text("Click to add Image.", style = TextStyle(fontSize = 25.sp), color = dataManip.textLDModeColor, modifier = Modifier.padding(bottom = 20.dp))
            Button(onClick = {
                if (permissionGranted) {
                    navController.navigate("CameraScreen")
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

                    CoroutineScope(Dispatchers.IO).launch { dataManip.updateCameraPermission(permissionGranted) }
                    navController.navigate("CameraScreen") // if allowed
                }
                askPermissions = false
            }
        )

        if(askPermissions)
        {
            permissionLauncher.launch(Manifest.permission.CAMERA) // then request it
        }

        if(showDialog == 1)
        {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Camera Permission Request", textAlign = TextAlign.Center) },
                text = { Text("Please grant permission to access your camera to continue.", fontSize = 18.sp) }, //dialog for allowing
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
                title = { Text("Access Denied.", textAlign = TextAlign.Center) }, // dialog for sending to settings since dont allow is selected
                text = { Text("You have set the permission to not allowed. If you would still like to continue, please click the go to settings button and adjust it manually.", fontSize = 18.sp) },
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