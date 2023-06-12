package com.example.favorite_video_game_genres

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.favorite_video_game_genres.ui.theme.Favorite_video_game_genresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            createScreen()
        }
    }
    @Composable
    fun createScreen()
    {
        Text(
            text = stringResource(id = R.string.labelName),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp, start = 20.dp, end = 20.dp),
            style = TextStyle(
                color = Color(android.graphics.Color.parseColor("#0089FF")),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )//NEED TO ENTER CHECK LIST HERE - FOR TOMORROW
        Button(
            onClick = { /* Navigate to Details Activity */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 750.dp, bottom = 16.dp, start = 60.dp, end = 60.dp)
                )
        { Text(
            text ="Submit",
            textAlign = TextAlign.Center,
            style = TextStyle(color = Color.Red, fontSize = 18.sp, fontFamily = FontFamily.SansSerif)
            )
        }
    }
}