package com.example.favorite_video_game_genres

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            // Access a Cloud Firestore instance from your Activity
            val db = FirebaseFirestore.getInstance()

// Create a new user with a first, middle, and last name
            val user = hashMapOf(
                "first" to "Alan",
                "middle" to "Mathison",
                "last" to "Turing",
                "born" to 1912,
            )
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
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
        barChartTestData += Pair("MMORPG's",12f)

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