package com.example.favorite_video_game_genres

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.foundation.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*

import com.example.favorite_video_game_genres.ui.theme.Favorite_video_game_genresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            createScreen()
        }
    }
    @Composable
    fun createScreen() //TODO -- Make font size smaller, lower them to match the middle of the bars, and add button
    {
        var barChartTestData:Array<Pair<String, Float>> = arrayOf(Pair("Action Games", 1f), Pair("Adventure Games", 4f), Pair("RPG", 5f), Pair("First person shooters", 3f))
        barChartTestData += Pair("MOBA Games", 2f)
        barChartTestData += Pair("Sport Games", 6f)
        barChartTestData += Pair("Sandbox Games", 8f)
        barChartTestData += Pair("Trivia Games", 1f)
        barChartTestData += Pair("Board Games", 7f)
        barChartTestData += Pair("Indie Games", 3f)
        barChartTestData += Pair("Racing Games", 8f)
        barChartTestData += Pair("MMORPG's", 12f)

            Column(
            modifier = Modifier
                .fillMaxSize())
        {
            Text(
                text = "Favorite Video Game Genres",
                style = TextStyle(
                    color = Color(android.graphics.Color.parseColor("#FF6F61")),
                    fontSize = 34.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 30.dp, bottom = 30.dp)
            )
            barChartTestData.forEach { value ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )
                {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 120.dp)
                    )
                    {
                        drawRect(
                            color = Color(android.graphics.Color.parseColor("#88b04b")),
                            size = Size((size.width - 60.dp.toPx()) * (value.second / barChartTestData.maxOfOrNull { it.second }!!), size.height)
                        )
                    }
                    Text(
                        text = value.first,
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#6B5B95")),
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}