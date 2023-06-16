package com.example.favorite_video_game_genres

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.*
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
import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


//import com.example.favorite_video_game_genres.ui.theme.Favorite_video_game_genresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Access a Cloud Firestore instance from your Activity
            val db = FirebaseFirestore.getInstance()

            val test = db.collection("users")
            // Create a new user with a first, middle, and last name
            val user = hashMapOf(
                "first" to "Alan",
                "middle" to "Mathison",
                "last" to "Turing",
                "born" to 1912,
            )
           test.document("first").set(test)

            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }


            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "DisplayScreen"
            )
            {
                composable("DisplayScreen")
                {
                    DisplayScreen(navController = navController)
                }
                composable("InputScreen")
                {
                    InputScreen(navController = navController)
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun DisplayScreen(navController: NavController) {
        var barGraphData: Array<Pair<String, Float>> = arrayOf(
            Pair("Action Games", 1f),
            Pair("Adventure Games", 4f),
            Pair("RPG", 1f),
            Pair("FPS Games", 3f),
            Pair("MOBA Games", 2f),
            Pair("Sport Games", 6f),
            Pair("Sandbox Games", 8f),
            Pair("Trivia Games", 1f),
            Pair("Board Games", 7f),
            Pair("Indie Games", 3f),
            Pair("Racing Games", 8f),
            Pair("MMORPG's", 12f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#fff68f")))
        )
        {
            Text(
                text = "Favorite Video Game Genres",
                style = TextStyle(
                    color = Color(android.graphics.Color.parseColor("#FF6F61")),
                    fontSize = 34.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.W900
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp)
                    .align(Alignment.CenterHorizontally)
            )
            barGraphData.forEach { value ->
                var width by remember { mutableStateOf(0f) }
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
                        width =
                            (size.width - 216.dp.toPx()) * (value.second / barGraphData.maxOfOrNull { it.second }!!)
                        drawRect(
                            color = Color(android.graphics.Color.parseColor("#88b04b")),
                            size = Size(
                                (size.width - 40.dp.toPx()) * (value.second / barGraphData.maxOfOrNull { it.second }!!),
                                size.height
                            ),
                        )
                    }
                    Text(
                        text = value.first,
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#6B5B95")),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 3.dp, start = 5.dp)
                    )
                    Text(
                        text = value.second.toInt().toString(),
                        modifier = Modifier
                            .padding(start = width.dp + 130.dp, top = 5.dp),
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#F28C28")),
                            fontSize = 14.sp,
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
                    .padding(start = 60.dp, end = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#8a2be2"))
                ),
            )
            {
                Text(
                    text = "Insert your own choices!",
                    color = Color(android.graphics.Color.parseColor("#00ced1")),
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    fun InputScreen(navController: NavController) {
        Box(modifier = Modifier.background(Color(android.graphics.Color.parseColor("#fff68f"))))
        {
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
            val options :Array<String> = arrayOf(
                "Action Games",
                "Adventure Games",
                "RPG (Role-Playing Games)",
                "FPS Games (First Person Shooters)",
                "MOBA Games (Multiplayer Online Battle Arena)",
                "Sport Games",
                "Sandbox Games",
                "Trivia Games",
                "Board Games",
                "Indie Games",
                "Racing Games",
                "MMORPG's (Massive Multiplayer Online Role-Playing Games)"
            )
            var checked = remember { mutableStateOf<Array<Boolean>>(Array<Boolean>(12){false}) }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 130.dp)
            )
            {
                items(12) { i ->
                    var isChecked by remember { mutableStateOf(false) }
                    Row() {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = !isChecked},
                        )
                        Text(
                            text = options[i],
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = Color.Red
                            ),
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                        //checked[i] = isChecked
                    }
                    //Log.d("Tag1", "The button is checked: $isChecked")
                    checked.value[i] = isChecked
                }
            }
            Button(
                onClick =
                {
                    navController.navigate("DisplayScreen")
                    {
                        popUpTo("InputScreen")
                    }
                    Log.d("Tag2", "Is this specific button checked? : ${checked.value[2]}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 740.3.dp, bottom = 39.dp, start = 60.dp, end = 60.dp)
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
}