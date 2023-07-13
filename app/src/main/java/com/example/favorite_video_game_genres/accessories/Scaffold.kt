package com.example.favorite_video_game_genres.accessories

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.favorite_video_game_genres.data.DataManipulation

class Scaffold {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldBar(dataManip: DataManipulation, navController: NavController, content: @Composable (PaddingValues) -> Unit)
    {
        var barColor: Color

        if(dataManip.LDmode == "Light")
        {
            barColor = Color.White
        }
        else
        {
            barColor = Color.Black
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(40.dp), // Height of AppBar
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = barColor),
                    title = {
                        Text(
                            "Favorite Video Game Genre",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = dataManip.textLDModeColor
                        )  // Change Text style for the title
                    },
                    navigationIcon = {
                        if (navController.currentBackStackEntry?.destination?.route != "DisplayScreen") {
                            IconButton(onClick = { navController.popBackStack() })
                            {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = dataManip.textLDModeColor
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("WebView") }) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = "How-To-Guide",
                                tint = dataManip.textLDModeColor
                            )
                        }
                    }
                )
            },
            content = { paddingValues -> content(paddingValues) }
        )
    }
}