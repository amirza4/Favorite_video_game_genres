package com.example.favorite_video_game_genres.accessories

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation
import com.example.favorite_video_game_genres.R
import com.example.favorite_video_game_genres.ui.theme.DarkColorScheme
import com.example.favorite_video_game_genres.ui.theme.LightColorScheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Overlay {

    @SuppressLint("NotConstructor")
    @Composable
    fun Overlay(dataManip: DataManipulation) {

        var isDarkMode by remember { mutableStateOf(dataManip.getLDMode()!!) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .absoluteOffset((0).dp, (0).dp)
                .scale(1f),
            contentAlignment = Alignment.BottomEnd
        )
        {
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .padding(end = 25.dp, bottom = 50.dp)
                .scale(1.2f))
            {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { checked -> //LD Mode swap
                        isDarkMode = checked
                        if (checked) {
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
                    },
                    modifier = Modifier,
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = Color.White,
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Black,
                        uncheckedBorderColor = Color.Black,
                        uncheckedThumbColor = Color.Black,
                        uncheckedTrackColor = Color.White
                    )
                )
                var xPosition by remember { mutableStateOf(0.0) }
                if(isDarkMode)
                {
                    xPosition = -12.5
                }
                else
                {
                    xPosition = -11.0
                }
                Text(
                    text = dataManip.LDmode,
                    style = TextStyle(
                        color = dataManip.textLDModeColor,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.offset(xPosition.dp, (-39).dp)
                )
                if (isDarkMode) {
                    Image(
                        painter = painterResource(id = R.drawable.moon1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(17.dp, 17.dp)
                            .offset((-30).dp, (-15.5).dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.sun1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp, 20.dp)
                            .offset((-6).dp, (-14).dp)
                    )
                }
            }
        }
    }

    @Composable
    fun CameraScreenButton(dataManip: DataManipulation, navController: NavController)
    {
        var isImageTaken: Boolean = dataManip.returnImageFile()?.exists()!!
        if(dataManip.decodeImage(dataManip.returnImageFile()) == null)
        {
            isImageTaken = false
            dataManip.returnImageFile()?.delete()
        }
        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        )
        {
            Box(modifier = Modifier
                .padding(top = 75.dp, end = 15.dp)
                .clip(CircleShape)
                .background(dataManip.textLDModeColor)
            )
            {
                var cameraIcon: Int
                if(dataManip.LDmode == "Dark")
                {
                    cameraIcon = R.drawable.darkmodecameraicon
                }
                else
                {
                    cameraIcon = R.drawable.lightmodecameraicon
                }
                Image(
                    painter = painterResource(id = cameraIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(75.dp)
                        .padding(0.dp)
                        .clickable
                        {
                            if (!isImageTaken) {
                                navController.navigate("AddImageScreen")
                            } else {
                                navController.navigate("ImageDisplay")
                            }
                        }
                )
            }
        }
    }
}