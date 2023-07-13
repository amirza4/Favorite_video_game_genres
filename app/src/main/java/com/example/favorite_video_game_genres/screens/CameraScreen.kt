package com.example.favorite_video_game_genres.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.runBlocking
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.resolutionselector.AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY
import androidx.camera.core.resolutionselector.AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY
import androidx.camera.core.resolutionselector.ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY
import com.example.favorite_video_game_genres.data.DataManipulation
import com.example.favorite_video_game_genres.activities.MainActivity
import com.example.favorite_video_game_genres.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CameraScreen
{
    @SuppressLint("NotConstructor")@androidx.annotation.OptIn(androidx.camera.core.ExperimentalZeroShutterLag::class)
    @Composable
    fun CameraScreen(dataManip: DataManipulation, navController: NavController)
    {
        BackHandler() {
            val isImageTaken: Boolean = dataManip.returnImageFile()?.exists()!!
            if(isImageTaken)
            {
                navController.navigate("ImageDisplay")
            }
            else
            {
                navController.navigate("AddImageScreen")
            }
        }
        
        var cameraPermissionState by remember { mutableStateOf(false) }
        runBlocking {
            cameraPermissionState = dataManip.getCameraPermission() ?: false
        }
        if(!cameraPermissionState) // if camera permission changes to deny at any point, restart or exit app
        {
            AlertDialog(onDismissRequest = {},
                title = {Text("Settings changed.", fontSize = 24.sp, fontWeight = FontWeight.Bold)},
                text = { Text("There has been a detection of changed settings. You may choose to either exit or restart this app.") },
                confirmButton = {
                    Button(onClick = {
                        val intent = Intent(dataManip.context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK)
                        dataManip.activity.finishAffinity()
                        dataManip.activity.startActivity(intent)
                    }) {
                        Text("Restart")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        dataManip.activity.finishAffinity()
                    })
                    {
                        Text("Exit")
                    }
                }
            )
        }
        else
        {
            var aspectRatioStrategy by remember { mutableStateOf( RATIO_16_9_FALLBACK_AUTO_STRATEGY) }
            if(LocalConfiguration.current.screenWidthDp.dp > 400.dp)
            {
                aspectRatioStrategy = RATIO_4_3_FALLBACK_AUTO_STRATEGY
            }
            var rotation by remember { mutableStateOf(ROTATION_0) }
            val lifecycle = LocalLifecycleOwner.current
            val imageCapture by remember { mutableStateOf(ImageCapture.Builder()
                .setCaptureMode(CAPTURE_MODE_ZERO_SHUTTER_LAG)
                .setResolutionSelector(ResolutionSelector.Builder()
                    .setResolutionStrategy(HIGHEST_AVAILABLE_STRATEGY)
                    .setAspectRatioStrategy(RATIO_16_9_FALLBACK_AUTO_STRATEGY).build())
                .setJpegQuality(100)
                .setTargetRotation(rotation)
                .build()) }

            object: OrientationEventListener(dataManip.context){
                override fun onOrientationChanged(orientation: Int) {
                    if(orientation == 0)
                    {
                        rotation = ROTATION_180
                    }
                    else if(orientation == 90)
                    {
                        rotation = ROTATION_90
                    }
                    else if(orientation == 180)
                    {
                        rotation = ROTATION_180
                    }
                    else if(orientation == 270)
                    {
                        rotation = ROTATION_270
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter)
            {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight(),
                    factory = { _ ->

                        val previewView = PreviewView(dataManip.context)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(dataManip.context)

                        cameraProviderFuture.addListener({
                            val preview = Preview.Builder().build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()

                            val cameraProvider = cameraProviderFuture.get()
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycle,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        }, ContextCompat.getMainExecutor(dataManip.context))
                        previewView
                    })
                var backgroundColor: Color by remember { mutableStateOf(Color.Unspecified) }
                var imageResource: Int by remember { mutableStateOf(0) }
                if(dataManip.LDmode == "Dark")
                {
                    backgroundColor = Color.White
                    imageResource = R.drawable.darkmodecameraicon
                }
                else
                {
                    backgroundColor = Color.Black
                    imageResource = R.drawable.lightmodecameraicon
                }
                FloatingActionButton(
                    onClick = {
                        val openFileStream = dataManip.context.openFileOutput("image1.jpeg", Context.MODE_PRIVATE)
                        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(openFileStream).build()
                        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(dataManip.context), object: OnImageSavedCallback
                        {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                openFileStream.close()
                                CoroutineScope(Dispatchers.IO).launch { dataManip.updateImageRotation(rotation) }
                                navController.navigate("ImageDisplay")
                                {
                                    popUpTo("CameraScreen")
                                    {
                                        inclusive = true
                                    }
                                }
                            }
                            override fun onError(error: ImageCaptureException)
                            {
                                Log.d("Taggy", "Image capturing didn't work. + ${error.imageCaptureError }}")
                            }
                        })
                    },
                    modifier = Modifier
                        .offset(y = (-75).dp)
                        .clip(CircleShape)
                        .size(75.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "Camera",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(backgroundColor)
                    )
                }
            }
        }
    }
}