package com.example.favorite_video_game_genres.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.R
import com.example.favorite_video_game_genres.data.DataManipulation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class InputScreen {

    @SuppressLint("NotConstructor", "MutableCollectionMutableState")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun InputScreen(dataManip: DataManipulation, navController: NavController) {

        BackHandler {
            dataManip.fetchFromFireBase()
            {
                navController.navigate("DisplayScreen")
                {
                    popUpTo("InputScreen")
                    {
                        inclusive = true
                    }
                }
            }
        }

        var checked by remember { mutableStateOf(ArrayList<Boolean>()) }
        var newOptionChecked by remember { mutableStateOf(false) }
        var newOptionName by remember { mutableStateOf("") }
        val state = rememberScrollState()
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusController = LocalFocusManager.current

        Column(modifier = Modifier
            .background(dataManip.primaryColor)
            .fillMaxSize()
            .verticalScroll(state)
            .clickable { // clears focus if clicked outside textfield
                focusController.clearFocus()
            }
        )
        {
            //CameraScreenButton(navController)
            Text(
                text = stringResource(R.string.labelName),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp, start = 20.dp, end = 20.dp),
                style = TextStyle(
                    color = dataManip.textLDModeColor,
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
            var listHeight: Dp = 0.dp
            if(getScreenHeight < 600.0.dp)
            {
                listHeight = (getScreenHeight * .5f)
            }
            else if(getScreenHeight >= 600.0.dp && getScreenHeight < 700.0.dp) //changing checklist height depending on phone sixe
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
                items(dataManip.retrieveData.size) { i -> //create checkboxes for each record
                    Row() {
                        var check by remember { mutableStateOf(checked.getOrNull(i) ?: false) }
                        Checkbox(
                            checked = check,
                            onCheckedChange = { check = !check},
                            colors = CheckboxDefaults.colors(
                                checkedColor = (dataManip.secondaryColor),
                                uncheckedColor = (dataManip.secondaryColor)
                            )
                        )
                        if(checked.getOrNull(i) == null)
                        {
                            checked.add(check) // if checkbox getting checked for the first time, add to array
                        }
                        else
                        {
                            checked[i] = check // otherwise just update the value
                        }
                        var name: String = dataManip.retrieveData[i].first

                        if(dataManip.retrieveData[i].first == "MMORPG") //Name adjustment
                        {
                            name += " (Massive Multiplayer Online Role-Playing Game)"
                        }
                        else if(dataManip.retrieveData[i].first == "MOBA")
                        {
                            name += " (Multiplayer Online Battle Arena"
                        }
                        Text(
                            text = name,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = dataManip.textLDModeColor
                            ),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .clickable { check = !check }
                        )
                    }

                    if(i == dataManip.retrieveData.size - 1) // For new custom made checkbox
                    {
                        Row()
                        {
                            Checkbox(
                                checked = newOptionChecked,
                                onCheckedChange = {newOptionChecked = !newOptionChecked},
                                colors = CheckboxDefaults.colors(
                                    checkedColor = (dataManip.secondaryColor),
                                    uncheckedColor = (dataManip.secondaryColor)
                                )
                            )
                            Text(
                                text = "Custom: ",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    color = dataManip.textLDModeColor
                                ),
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .clickable { newOptionChecked = !newOptionChecked }
                            )
                            val containercolor : Color
                            if(dataManip.LDmode == "Dark"){
                                containercolor = Color.DarkGray
                            }
                            else
                            {
                                containercolor = Color.LightGray
                            }
                            TextField(
                                value = newOptionName,
                                onValueChange = {newOptionName = it },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    placeholderColor = (dataManip.secondaryColor),
                                    cursorColor = (dataManip.primaryColor),
                                    containerColor = containercolor,
                                    disabledIndicatorColor = Color.Transparent,
                                    disabledPlaceholderColor = Color.Gray
                                ),
                                textStyle = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.SansSerif,
                                    color = dataManip.textLDModeColor,
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
                                        color = (dataManip.tertiaryColor),
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
            var submitted by remember { mutableStateOf(0) }
            Button(
                onClick =
                {
                    if(checked.any(){ it } || newOptionChecked)
                    {
                        dataManip.newGenreCheck(newOptionChecked, newOptionName)
                        {
                            dataManip.updateDB(checked)
                            {
                                submitted = 2
                            }
                        }
                    }
                    else
                    {
                        submitted = 1
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = (dataManip.tertiaryColor)
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
                        color = dataManip.textLDModeColor,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
            var barColor: Color

            if(dataManip.LDmode == "Light")
            {
                barColor = Color.White
            }
            else
            {
                barColor = Color.Black
            }

            if(submitted == 2)
            {
                AlertDialog(
                    onDismissRequest = {},
                    containerColor = barColor,
                    title = { Text("Votes Submitted!", textAlign = TextAlign.Center, color = dataManip.textLDModeColor) },
                    text = { Text("You have successfully submitted your votes!", fontSize = 18.sp, color = dataManip.textLDModeColor) },
                    confirmButton = {}
                )
                LaunchedEffect(Unit)
                {
                    dataManip.fetchFromFireBase()
                    {
                        runBlocking()
                        {
                            delay(2000)
                            navController.navigate("DisplayScreen")
                            {
                                popUpTo("InputScreen")
                                {
                                    inclusive = true
                                }
                            }
                            submitted = 0
                        }
                    }
                }
            }
            else if(submitted == 1)
            {
                AlertDialog(
                    onDismissRequest ={},
                    containerColor = barColor,
                    title = { Text("Error: No Checkboxes Selected", textAlign = TextAlign.Center, color = dataManip.textLDModeColor) },
                    text = { Text("You have not selected any votes to submit. To vote, please check the checkboxes of your favorite video game genres, or to return, click the back arrow on the top right to return to the display votes screen.", color = dataManip.textLDModeColor) },
                    confirmButton = {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
                        {
                            Button(onClick = {
                                submitted = 0
                            })
                            {
                                Text(text = "I Understand", textAlign = TextAlign.Center)
                            }
                        }
                    },
                )
            }
        }
    }
}