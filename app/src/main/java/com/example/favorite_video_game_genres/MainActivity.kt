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
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.google.firebase.firestore.FirebaseFirestore
import com.example.favorite_video_game_genres.ui.theme.*


class MainActivity : ComponentActivity() {

    private var retrieveData = mutableListOf<Pair<String, Int>>()

    private var LDmode by mutableStateOf("Light")
    private var primaryColor by mutableStateOf(LightColorScheme.primary)
    private var secondaryColor by mutableStateOf(LightColorScheme.secondary)
    private var tertiaryColor by mutableStateOf(LightColorScheme.tertiary)
    private var textLDModeColor by mutableStateOf(Color.Black)

    // Function runs first for retrieving the data from firestore
    private fun fetchFromFireBase(callback: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("game_counts").document("84c8g5rVr8KJliP4108c").get()
            .addOnSuccessListener { document ->
                val data = document.data
                data!!.toSortedMap().forEach { (key, value) ->
                    retrieveData.add(Pair(key.toString(), value.toString().toFloat().toInt()))
                }
                callback()
            }
            .addOnFailureListener { exception ->
                Log.d("errorTag", "Pulling data failed : ", exception)
            }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchFromFireBase {
            setContent {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.height(40.dp), // Height of AppBar
                            title = {
                                Text("Favorite Video Game Genre", style = MaterialTheme.typography.headlineSmall)  // Change Text style for the title
                            },
                            navigationIcon = {
                                if(navController.currentBackStackEntry?.destination?.route != "DisplayScreen") {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                                    }
                                        Color(android.graphics.Color.parseColor("#DB864E"))
                                }
                            }
                        )
                        //Spacer needs to be added below the TopAppBar to allow a white space separating the graph from the scaffold
                            Spacer(modifier = Modifier.height(16.dp))
                    },
                    content = {
                        NavHost(navController, startDestination = "DisplayScreen") {
                            composable("DisplayScreen") { DisplayScreen(navController) }
                            composable("InputScreen") { InputScreen(navController) }
                        }
                    }
                )
                Overlay()
                }
            }
        }
    }

    @Composable
    fun Overlay() {
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

        if(LocalConfiguration.current.screenHeightDp.dp < 650.dp)
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

        var isDarkMode by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier.fillMaxSize()
                .absoluteOffset((0).dp, (-100).dp)
                .scale(scaleOverlay),
            contentAlignment = Alignment.BottomEnd
        )
        {
            Switch(
                checked = isDarkMode,
                onCheckedChange = { checked ->
                    isDarkMode = checked
                    if (checked) {
                        LDmode = "Dark"
                        textLDModeColor = Color.White
                        primaryColor = DarkColorScheme.primary
                        secondaryColor = DarkColorScheme.secondary
                        tertiaryColor = DarkColorScheme.tertiary
                        // Log.d("DarkMode", "swapped to Dark")
                    } else {
                        LDmode = "Light"
                        textLDModeColor = Color.Black
                        primaryColor = LightColorScheme.primary
                        secondaryColor = LightColorScheme.secondary
                        tertiaryColor = LightColorScheme.tertiary
                        // Log.d("LightMode", "swapped to Light")
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
                text = LDmode,
                style = TextStyle(
                    color = textLDModeColor,
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


    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun DisplayScreen(navController: NavController) {

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
                    color = textLdModeColor,
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
            retrieveData.forEach { value ->
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
                                (size.width - 15.dp.toPx()) * (value.second.toFloat() / retrieveData.maxOfOrNull { it.second }!!),
                                size.height
                            ),
                        )
                    }
                    Text(
                        text = value.first,
                        style = TextStyle(
                            color = textLDModeColor,
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
                            color = textLDModeColor,
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
                    color = textLDModeColor,
                    fontSize = 16.sp
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun InputScreen(navController: NavController) {
        var checked by remember { mutableStateOf(ArrayList<Boolean>()) }
        var newOptionChecked by remember { mutableStateOf(false) }
        var newOptionName by remember { mutableStateOf("")}
        val state = rememberScrollState()
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusController = LocalFocusManager.current
        Column(modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#DB864E")))
            .fillMaxSize()
            .verticalScroll(state)
            .clickable {
                focusController.clearFocus()
            }
        )
        {
            Text(
                text = stringResource(R.string.labelName),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp, start = 20.dp, end = 20.dp),
                style = TextStyle(
                    color = textLDModeColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, top = 20.dp)
                    .height(listHeight)
            )
            {
                items(retrieveData.size) { i ->
                    Row() {
                        var check by remember { mutableStateOf(checked.getOrNull(i) ?: false) }
                        Checkbox(
                            checked = check,
                            onCheckedChange = { check = !check},
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(android.graphics.Color.parseColor("#DC6B2F")),
                                uncheckedColor = Color(android.graphics.Color.parseColor("#DC6B2F"))
                            )
                        )
                        if(checked.getOrNull(i) == null)
                        {
                            checked.add(check)
                        }
                        else
                        {
                            checked[i] = check
                        }
                        var name: String = retrieveData[i].first
                        if(retrieveData[i].first == "MMORPG")
                        {
                            name += " (Massive Multiplayer Online Role-Playing Game)"
                        }
                        else if(retrieveData[i].first == "MOBA")
                        {
                            name += " (Multiplayer Online Battle Arena"
                        }
                        Text(
                            text = name,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = textLdModeColor
                            ),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .clickable { check = !check }
                        )
                    }

                    if(i == retrieveData.size - 1)
                    {
                        Row()
                        {
                            Checkbox(
                                checked = newOptionChecked,
                                onCheckedChange = {newOptionChecked = !newOptionChecked},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(android.graphics.Color.parseColor("#DC6B2F")),
                                    uncheckedColor = Color(android.graphics.Color.parseColor("#DC6B2F"))
                                )
                            )
                            Text(
                                text = "Custom: ",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    color = textLdModeColor
                                ),
                                modifier = Modifier.padding(top = 10.dp)
                                    .clickable { newOptionChecked = !newOptionChecked }
                            )
                            TextField(
                                value = newOptionName,
                                onValueChange = {newOptionName = it },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    placeholderColor = Color(android.graphics.Color.parseColor("#E35205")),
                                    cursorColor = Color(android.graphics.Color.parseColor("#DC6B2F")),
                                    containerColor = Color.LightGray,
                                    disabledIndicatorColor = Color.Transparent,
                                    disabledPlaceholderColor = Color.Gray
                                ),
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    color = textLdModeColor,
                                ),
                                placeholder = { Text(text = "Enter your option here", style = TextStyle(fontSize = 14.sp)) },
                                shape = CircleShape,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .fillMaxWidth(.85f)
                                    .height(50.dp)
                                    .offset(10.dp, 0.dp)
                                    .border(
                                        width = 5.dp,
                                        color = Color(android.graphics.Color.parseColor("#E35205")),
                                        shape = CircleShape
                                    )
                                    .onFocusChanged
                                    {
                                        if (!it.hasFocus) {
                                            keyboardController?.hide()
                                        }
                                    },
                                singleLine = true,
                                enabled = newOptionChecked,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone =
                                    {
                                        keyboardController?.hide()
                                        focusController.clearFocus()
                                    }
                                )
                            )
                        }
                    }
                }
            }
            Button(
                onClick =
                {
                    newGenreCheck(newOptionChecked, newOptionName)
                    {
                        updateDB(checked)
                        {
                            navController.navigate("DisplayScreen")
                            {
                                popUpTo("InputScreen")
                            }
                        }
                    }
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
                        color = textLdModeColor,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        }
    }
    private fun updateDB(checked: MutableList<Boolean>, callback: () -> Unit)
    {
        val graphData = FirebaseFirestore.getInstance().collection("game_counts").document("84c8g5rVr8KJliP4108c")
        for((i, value) in checked.withIndex())
        {
            if(value)
            {
                retrieveData[i] = retrieveData[i].copy(second = retrieveData[i].second + 1)
                graphData.update(retrieveData[i].first, retrieveData[i].second)
            }
        }

        callback()
    }

    private fun newGenreCheck(check: Boolean, name: String, callback: () -> Unit)
    {
        if(!check)
        {
            callback()
        }
        else
        {
            if(!(name.isNullOrBlank() || retrieveData.any { it.first.lowercase().contains(name.trim().lowercase()) }))
            {
                retrieveData.add(Pair(name.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }, 1))
                FirebaseFirestore.getInstance().collection("game_counts").document("84c8g5rVr8KJliP4108c").update(name.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }, 1)
                retrieveData.sortBy { it.first }
                callback()
            }
            else
            {
                //need to add some form of error for nulls, blanks, and copies of previous choices
            }
        }
    }
}