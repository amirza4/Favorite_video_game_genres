package com.example.favorite_video_game_genres

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.firebase.firestore.FirebaseFirestore


//import com.example.favorite_video_game_genres.ui.theme.Favorite_video_game_genresTheme

class MainActivity : ComponentActivity() {

    private var retrieveData by mutableStateOf(Array<Float>(12){10f})

    //Function runs first for retriving the data from firestore
    private fun fetchFromFireBase(callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("game_counts").document("84c8g5rVr8KJliP4108c").get()
            .addOnSuccessListener { document ->
                val data = document.data
                retrieveData[0] = (data!!["Action Games"]).toString().toFloat()
                retrieveData[1] = (data!!["Adventure Games"]).toString().toFloat()
                retrieveData[2] = (data!!["Board Games"]).toString().toFloat()
                retrieveData[3] = (data!!["FPS"]).toString().toFloat()
                retrieveData[4] = (data!!["Indie"]).toString().toFloat()
                retrieveData[5] = (data!!["MMORPG"]).toString().toFloat()
                retrieveData[6] = (data!!["MOBA"]).toString().toFloat()
                retrieveData[7] = (data!!["RPG"]).toString().toFloat()
                retrieveData[8] = (data!!["Racing Games"]).toString().toFloat()
                retrieveData[9] = (data!!["Sandbox"]).toString().toFloat()
                retrieveData[10] = (data!!["Sport Games"]).toString().toFloat()
                retrieveData[11] = (data!!["Trivia"]).toString().toFloat()
                callback()
            }
            .addOnFailureListener {exception ->
                Log.d("errorTag", "Pulling data failed : ", exception)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Access a Cloud Firestore instance from your Activity
//            val db = FirebaseFirestore.getInstance()
//
//            val test = db.collection("users")
        // Create a new user with a first, middle, and last name
//            val user = hashMapOf(
//                "first" to "Alan",
//                "middle" to "Mathison",
//                "last" to "Turing",
//                "born" to 1912,
//            )
//           test.document("first").set(user)

//            db.collection("users")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding document", e)
//                }


        fetchFromFireBase {
            setContent {

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
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun DisplayScreen(navController: NavController) {

        var barGraphData: Array<Pair<String, Float>> = arrayOf(
            Pair("Action Games", retrieveData[0]),
            Pair("Adventure Games", retrieveData[1]),
            Pair("Board Games", retrieveData[2]),
            Pair("FPS Games", retrieveData[3]),
            Pair("Indie Games", retrieveData[4]),
            Pair("MMORPG's", retrieveData[5]),
            Pair("MOBA Games", retrieveData[6]),
            Pair("RPG", retrieveData[7]),
            Pair("Racing Games", retrieveData[8]),
            Pair("Sandbox Games", retrieveData[9]),
            Pair("Sport Games", retrieveData[10]),
            Pair("Trivia Games", retrieveData[11])
        )

        val state = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state)
                .background(Color(android.graphics.Color.parseColor("#DB864E")))
        )
        {
            Text(
                text = "Favorite Video Game Genres",
                style = TextStyle(
                    color = Color(android.graphics.Color.parseColor("#000000")),
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
            barGraphData.forEach { value ->
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
                            color = Color(android.graphics.Color.parseColor("#DC6B2F")),
                            size = Size(
                                (size.width - 15.dp.toPx()) * (value.second / barGraphData.maxOfOrNull { it.second }!!),
                                size.height
                            ),
                        )
                    }
                    Text(
                        text = value.first,
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#000000")),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                    )
                    Text(
                        text = value.second.toInt().toString(),
                        modifier = Modifier
                            .padding(start = 130.dp, bottom = 5.dp),
                        style = TextStyle(
                            color = Color(android.graphics.Color.parseColor("#000000")),
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
                    containerColor = Color(android.graphics.Color.parseColor("#E35205"))
                ),
            )
            {
                Text(
                    text = "Insert your own choices!",
                    color = Color(android.graphics.Color.parseColor("#000000")),
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    fun InputScreen(navController: NavController) {
        var checked = remember { mutableStateOf<Array<Boolean>>(Array<Boolean>(12){false}) }
        val state = rememberScrollState()
        Column(modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#DB864E")))
            .fillMaxSize()
            .verticalScroll(state)
            )
        {
            Text(
                text = stringResource(R.string.labelName),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp, start = 20.dp, end = 20.dp),
                style = TextStyle(
                    color = Color(android.graphics.Color.parseColor("#000000")),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            )
            val options :Array<String> = arrayOf(
                "Action Games",
                "Adventure Games",
                "Board Games",
                "FPS Games (First Person Shooters)",
                "Indie Games",
                "MMORPG's (Massive Multiplayer Online Role-Playing Games)",
                "MOBA Games (Multiplayer Online Battle Arena)",
                "RPG (Role-Playing Games)",
                "Racing Games",
                "Sandbox Games",
                "Sport Games",
                "Trivia Games",
            )
            /* Phone height sizes
            3.3 WQVGA - 533.0.dp
            Nexus 5 - 616.0.dp
            Pixel 3 - 737.0.dp
            Pixel 6 - 829.0.dp
            8 Foldout - 945.0.dp
            */
            val getScreenHeight = LocalConfiguration.current.screenHeightDp.dp
            var listHeight:Dp = 0.dp
            if(getScreenHeight < 600.0.dp)
            {
                listHeight = (getScreenHeight * .5f)
            }
            else if(getScreenHeight >= 600.0.dp && getScreenHeight < 700.0.dp)
            {
                listHeight = (getScreenHeight * .6f)
            }
            else if(getScreenHeight >= 700.dp)
            {
                listHeight = (getScreenHeight * .7f)
            }
            Log.d("TagHIHI", "$listHeight----------------${getScreenHeight}")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 20.dp)
                    .height(listHeight)
            )
            {
                items(12) { i ->
                    var isChecked by remember { mutableStateOf(false) }
                    Row() {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = !isChecked},
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(android.graphics.Color.parseColor("#DC6B2F")),
                                uncheckedColor = Color(android.graphics.Color.parseColor("#DC6B2F"))
                            )
                        )
                        Text(
                            text = options[i],
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = Color(android.graphics.Color.parseColor("#000000"))
                            ),
                            modifier = Modifier
                                .padding(top = 10.dp)
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

                    //Log.d("Tag2", "Is this specific button checked? : ${checked.value[2]}")

                    val db = FirebaseFirestore.getInstance()

                    for((i, value) in checked.value.withIndex())
                    {
                        if(value)
                        {
                            retrieveData[i]++
                        }
                    }

                    val graphData = db.collection("game_counts").document("84c8g5rVr8KJliP4108c")
                    graphData.update("Action Games", retrieveData[0])
                    graphData.update("Adventure Games", retrieveData[1])
                    graphData.update("Board Games", retrieveData[2])
                    graphData.update("FPS", retrieveData[3])
                    graphData.update("Indie", retrieveData[4])
                    graphData.update("MMORPG", retrieveData[5])
                    graphData.update("MOBA", retrieveData[6])
                    graphData.update("RPG", retrieveData[7])
                    graphData.update("Racing Games", retrieveData[8])
                    graphData.update("Sandbox", retrieveData[9])
                    graphData.update("Sport Games", retrieveData[10])
                    graphData.update("Trivia", retrieveData[11])
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#E35205"))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp, start = 60.dp, end = 60.dp)
            )
            {
                Text(
                    text = "Submit",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        }
    }
}