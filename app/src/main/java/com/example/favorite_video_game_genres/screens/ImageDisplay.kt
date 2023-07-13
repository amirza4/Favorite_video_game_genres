package com.example.favorite_video_game_genres.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation
import java.io.File

class ImageDisplay {
    @SuppressLint("NotConstructor")
    @Composable
    fun ImageDisplay(dataManip: DataManipulation, navController: NavController, imageID: ImageBitmap) {
        BackHandler() {
            navController.navigate("DisplayScreen")
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(dataManip.primaryColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Image(
                bitmap = imageID,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = (LocalConfiguration.current.screenHeightDp.dp * .33f))
                    .rotate(dataManip.imageRotation.toFloat() + 90f)
                    .scale(1.65f))
        }
        Box(modifier = Modifier.fillMaxSize().padding(bottom = 25.dp), contentAlignment = Alignment.BottomCenter)
        {
            Button(onClick =
            {
                navController.navigate("CameraScreen")
            },
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(bottom = 55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = dataManip.tertiaryColor))
            {
                Text("Update", style = TextStyle(fontSize = 14.sp), color = dataManip.textLDModeColor)
            }
            Button(onClick =
            {
                Log.d("Taggy", File(dataManip.context.filesDir, "image1.jpeg").exists().toString())
                navController.navigate("AddImageScreen")
                {
                    popUpTo("ImageDisplay")
                    {
                        dataManip.returnImageFile()?.delete()
                        inclusive = true
                    }
                }
            },
                modifier = Modifier
                    .fillMaxWidth(.7f)
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = dataManip.tertiaryColor))
            {
                Text("Delete", style = TextStyle(fontSize = 14.sp), color = dataManip.textLDModeColor)
            }
        }
    }
}