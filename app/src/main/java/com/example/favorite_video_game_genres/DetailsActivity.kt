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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.*
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.*

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            createScreen()
        }
    }

    @Composable
    fun createScreen() {
        Text(
            text = stringResource(R.string.labelName),
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
        )
        val (isActionGame, onStateChange) = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = isActionGame,
                    onValueChange = { onStateChange(!isActionGame) },
                    role = Role.Checkbox
                )
                .padding(start = 30.dp, top = 150.dp)
        )
        {
            Checkbox(
                checked = isActionGame,
                onCheckedChange = null,
            )
            Text(
                text = "Action Game",
                style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.SansSerif, color = Color.Red)
            ) // NEED TO HAMMER OUT MORE CHECKBOXES FOR OTHER CHOICES
        }
        Button(
            onClick = { /* Navigate back to Main Activity */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 750.dp, bottom = 16.dp, start = 60.dp, end = 60.dp)
        )
        {
            Text(
                text = "Submit",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            )
        }
    }
}