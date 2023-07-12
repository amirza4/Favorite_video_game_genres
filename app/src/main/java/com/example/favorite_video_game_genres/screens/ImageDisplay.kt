package com.example.favorite_video_game_genres.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation
import java.io.InputStream
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.runBlocking
import java.io.File

class ImageDisplay {
    @SuppressLint("NotConstructor")
    @Composable
    fun ImageDisplay(dataManip: DataManipulation, navController: NavController) {

        val file = dataManip.context.getFileStreamPath("image1.jpeg")
        if (file != null && file.exists())
        {
            Log.d("Taggy", "File does exist.\n${file.absolutePath}")
        }
        else
        {
            Log.d("Taggy", "File does not exist")
        }

        val inputStream = dataManip.context.openFileInput("image1.jpeg")
//        val imageLoader = ImageLoader.Builder(dataManip.context).build()
//        val imageRequest = ImageRequest.Builder(dataManip.context).data(inputStream).build()
//        var imageID: ImageBitmap? = null
//        runBlocking()
//        {
//            imageID = (imageLoader.execute(imageRequest).drawable as BitmapDrawable).bitmap.asImageBitmap()
//        }
        val imageID = BitmapFactory.decodeStream(inputStream)

        Log.d("Taggy", imageID.toString())

        Box(modifier = Modifier
            .fillMaxSize()
            .background(dataManip.primaryColor),
            contentAlignment = Alignment.Center
        )
        {
//            Image(
//                painter = painterResource(File()),
//                contentDescription = null,
//                modifier = Modifier
//                    .offset(0.dp, 0.dp)
//                    .rotate(90F))
        }
    }
}