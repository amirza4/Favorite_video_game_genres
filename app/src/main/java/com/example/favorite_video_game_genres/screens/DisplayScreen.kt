package com.example.favorite_video_game_genres.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation

class DisplayScreen {

    @SuppressLint("SuspiciousIndentation", "NotConstructor")
    @Composable
    fun DisplayScreen(dataManip: DataManipulation, navController: NavController) {

        val state = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state)
                .background(dataManip.primaryColor)
        )
        {
            Text(
                text = "Favorite Video Game Genres",
                style = TextStyle(
                    color = dataManip.textLDModeColor,
                    fontSize = 34.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W900
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
            dataManip.retrieveData.forEach { value -> //Create bar graph for each row in the DB
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )
                {
                    Canvas(

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 125.dp)
                    )
                    {
                        drawRect(
                            color = (dataManip.secondaryColor), // Create bars
                            size = Size(
                                (size.width - 15.dp.toPx()) * (value.second.toFloat() / dataManip.retrieveData.maxOfOrNull { it.second }!!),
                                size.height
                            ),
                        )
                    }
                    Text(
                        text = value.first,
                        style = TextStyle(
                            color = dataManip.textLDModeColor,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                    )
                    Text(
                        text = value.second.toString(),
                        modifier = Modifier
                            .padding(start = 130.dp, bottom = 5.dp),
                        style = TextStyle(
                            color = dataManip.textLDModeColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            Button(
                onClick =
                {
                    navController.navigate("InputScreen")
                    {
                        popUpTo("DisplayScreen")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 60.dp, end = 60.dp, bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = (dataManip.tertiaryColor)
                ),
            )
            {
                Text(
                    text = "Insert your own choices!",
                    color = dataManip.textLDModeColor,
                    fontSize = 16.sp
                )
            }
        }
    }
}