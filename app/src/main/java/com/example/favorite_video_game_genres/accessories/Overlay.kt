package com.example.favorite_video_game_genres.accessories

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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

        var switchPosY: Dp = 0.dp
        var switchPosX: Dp = 0.dp
        var scaleOverlay = 1f
        var textPosY: Dp = 0.dp
        var textPosX: Dp = 0.dp
        var sunPosY: Dp = 0.dp
        var sunPosX: Dp = 0.dp
        var sunSizeY: Dp = 0.dp
        var sunSizeX: Dp = 0.dp
        var moonPosY: Dp = 0.dp
        var moonPosX: Dp = 0.dp
        var moonSizeY: Dp = 0.dp
        var moonSizeX: Dp = 0.dp

        if(LocalConfiguration.current.screenHeightDp.dp < 650.dp) //Organizing LD Switch position based on phone size
        {
            switchPosY = LocalConfiguration.current.screenHeightDp.dp * .05f
            switchPosX = LocalConfiguration.current.screenWidthDp.dp * -.01f
            textPosY = LocalConfiguration.current.screenHeightDp.dp * -.025f
            textPosX = LocalConfiguration.current.screenWidthDp.dp * -.05f
            sunPosY = LocalConfiguration.current.screenHeightDp.dp * .025f
            sunPosX = LocalConfiguration.current.screenWidthDp.dp * .035f
            sunSizeY = LocalConfiguration.current.screenHeightDp.dp * .04f
            sunSizeX = LocalConfiguration.current.screenWidthDp.dp * .195f
            moonPosY = LocalConfiguration.current.screenHeightDp.dp * .0235f
            moonPosX = LocalConfiguration.current.screenWidthDp.dp * -.032f
            moonSizeY = LocalConfiguration.current.screenHeightDp.dp * .035f
            moonSizeX = LocalConfiguration.current.screenWidthDp.dp * .19f
        }
        else if(LocalConfiguration.current.screenHeightDp.dp >= 650.0.dp && LocalConfiguration.current.screenHeightDp.dp < 840.0.dp)
        {
            scaleOverlay = 1.25f
            switchPosY = LocalConfiguration.current.screenHeightDp.dp * -.08f
            switchPosX = LocalConfiguration.current.screenWidthDp.dp * -.13f
            textPosY = LocalConfiguration.current.screenHeightDp.dp * -.133f
            textPosX = LocalConfiguration.current.screenWidthDp.dp * -.157f
            sunPosY = LocalConfiguration.current.screenHeightDp.dp * -.0939f
            sunPosX = LocalConfiguration.current.screenWidthDp.dp * -.0752f
            sunSizeY = LocalConfiguration.current.screenHeightDp.dp * .033f
            sunSizeX = LocalConfiguration.current.screenWidthDp.dp * .185f
            moonPosY = LocalConfiguration.current.screenHeightDp.dp * -.098f
            moonPosX = LocalConfiguration.current.screenWidthDp.dp * -.16f
            moonSizeY = LocalConfiguration.current.screenHeightDp.dp * .024f
            moonSizeX = LocalConfiguration.current.screenWidthDp.dp * .13f
        }
        else if(LocalConfiguration.current.screenHeightDp.dp >= 840.dp)
        {
            scaleOverlay = 1.5f
            switchPosY = LocalConfiguration.current.screenHeightDp.dp * -.15f
            switchPosX = LocalConfiguration.current.screenWidthDp.dp * -.2f
            textPosY = LocalConfiguration.current.screenHeightDp.dp * -.19f
            textPosX = LocalConfiguration.current.screenWidthDp.dp * -.215f
            sunPosY = LocalConfiguration.current.screenHeightDp.dp * -.162f
            sunPosX = LocalConfiguration.current.screenWidthDp.dp * -.138f
            sunSizeY = LocalConfiguration.current.screenHeightDp.dp * .027f
            sunSizeX = LocalConfiguration.current.screenWidthDp.dp * .16f
            moonPosY = LocalConfiguration.current.screenHeightDp.dp * -.165f
            moonPosX = LocalConfiguration.current.screenWidthDp.dp * -.191f
            moonSizeY = LocalConfiguration.current.screenHeightDp.dp * .02f
            moonSizeX = LocalConfiguration.current.screenWidthDp.dp * .11f
        }

        var isDarkMode by remember { mutableStateOf(dataManip.getLDMode()!!) }
        Box(
            modifier = Modifier.fillMaxSize()
                .absoluteOffset((0).dp, (-100).dp)
                .scale(scaleOverlay),
            contentAlignment = Alignment.BottomEnd
        )
        {
            Switch(
                checked = isDarkMode,
                onCheckedChange = { checked -> //LD Mode swap
                    isDarkMode = checked
                    if (checked) {
                        CoroutineScope(Dispatchers.IO).launch { dataManip.updateLDMode(true) }
                        dataManip.LDmode = "Dark"
                        dataManip.textLDModeColor = Color.White
                        dataManip.primaryColor = DarkColorScheme.primary
                        dataManip.secondaryColor = DarkColorScheme.secondary
                        dataManip.tertiaryColor = DarkColorScheme.tertiary
                    } else {
                        CoroutineScope(Dispatchers.IO).launch { dataManip.updateLDMode(false) }
                        dataManip.LDmode = "Light"
                        dataManip.textLDModeColor = Color.Black
                        dataManip.primaryColor = LightColorScheme.primary
                        dataManip.secondaryColor = LightColorScheme.secondary
                        dataManip.tertiaryColor = LightColorScheme.tertiary
                    }
                },
                modifier = Modifier
                    .offset(switchPosX, switchPosY),
                colors = SwitchDefaults.colors(
                    checkedBorderColor = Color.White,
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    uncheckedBorderColor = Color.Black,
                    uncheckedThumbColor = Color.Black,
                    uncheckedTrackColor = Color.White
                )
            )
            Text(
                text = dataManip.LDmode,
                style = TextStyle(
                    color = dataManip.textLDModeColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.offset(textPosX, textPosY)
            )
            if(isDarkMode)
            {
                Image(
                    painter = painterResource(id = R.drawable.moon1),
                    contentDescription = null,
                    modifier = Modifier.offset(moonPosX, moonPosY)
                        .size(moonSizeX, moonSizeY)
                )
            }
            else
            {
                Image(
                    painter = painterResource(id = R.drawable.sun1),
                    contentDescription = null,
                    modifier = Modifier.offset(sunPosX, sunPosY)
                        .size(sunSizeX, sunSizeY)
                )
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
            .offset(
                x = ((LocalConfiguration.current.screenWidthDp.dp) - 50.dp),
                y = (LocalConfiguration.current.screenHeightDp.dp) * .2f
            )
            .padding(0.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.darkmodecameraicon),
                contentDescription = null,
                modifier = Modifier
                    .size(1.dp)
                    .scale(75f)
                    .padding(0.dp)
                    .clickable
                    {
                        if(!isImageTaken)
                        {
                            navController.navigate("AddImageScreen")
                        }
                        else
                        {
                            navController.navigate("ImageDisplay")
                        }
                    }
            )
        }
    }
}