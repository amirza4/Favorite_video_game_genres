package com.example.favorite_video_game_genres.data

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.example.favorite_video_game_genres.ui.theme.DarkColorScheme
import com.example.favorite_video_game_genres.ui.theme.LightColorScheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

class DataManipulation(var context: Context, var activity: Activity) {
    var retrieveData = mutableListOf<Pair<String, Int>>()
    var LDmode by mutableStateOf("Light")
    var primaryColor by mutableStateOf(LightColorScheme.primary)
    var secondaryColor by mutableStateOf(LightColorScheme.secondary)
    var tertiaryColor by mutableStateOf(LightColorScheme.tertiary)
    var textLDModeColor by mutableStateOf(Color.Black)
    var imageRotation by mutableStateOf(0)
    val db = FirebaseFirestore.getInstance().collection("game_counts").document("84c8g5rVr8KJliP4108c")

    fun fetchFromFireBase(callback: () -> Unit) {
        retrieveData = emptyArray<Pair<String, Int>>().toMutableList()
        CoroutineScope(Dispatchers.IO).launch { imageRotation = getImageRotation() ?: 0}
        val cache = DataCaching.createCacheDb(context) //creating cache object
        db.get(Source.SERVER)
            .addOnSuccessListener { document ->
                val data = document.data
                CoroutineScope(Dispatchers.Main).launch() // forcing on main thread
                {
                    data!!.toSortedMap().forEach { (key, value) ->
                        retrieveData.add(
                            Pair(
                                key,
                                value.toString().toFloat().toInt()
                            )
                        ) // retrieve data from firebase
                    }
                    callback() // goes back to where function was called
                    withContext(Dispatchers.IO) // runs this in the back even though its called back
                    {
                        val votesList = cache.userDao().getData()
                        if (votesList.isNullOrEmpty() || votesList.size != retrieveData.size) //if mismatch in record sizes
                        {
                            clearCache()
                            retrieveData.forEachIndexed { index, (key, value) -> //rewrite
                                cache.userDao().writeData(
                                    Votes(
                                        index,
                                        key,
                                        value.toString().toFloat().toInt()
                                    )
                                )
                            }
                        } else {
                            retrieveData.forEachIndexed { index, (key, value) -> //otherwise just update as normal
                                cache.userDao().updateData(
                                    Votes(
                                        index,
                                        key,
                                        value.toString().toFloat().toInt()
                                    )
                                )
                            }
                            cache.close()
                        }
                    }
                }
            }
            .addOnFailureListener { _ ->
                CoroutineScope(Dispatchers.Main).launch() //if firebase down, pull from cache
                {
                    val votesList: List<Votes> = cache.userDao().getData()
                    votesList.forEach { (_, fields, votes) ->
                        retrieveData.add(Pair(fields, votes))
                    }
                    cache.close()
                    callback()
                }
            }
    }

        

    fun updateDB(checked: MutableList<Boolean>, callback: () -> Unit)
    {
        val cache = DataCaching.createCacheDb(context)
        for((i, value) in checked.withIndex())
        {
            if(value)
            {
                retrieveData[i] = retrieveData[i].copy(second = retrieveData[i].second + 1) // cant directly add 1, so add a copy with 1 added to the second property
                db.update(retrieveData[i].first, retrieveData[i].second) 
            }
        }
        retrieveData.sortBy { it.first } //sort alphabetically
        CoroutineScope(Dispatchers.IO).launch{
            retrieveData.forEachIndexed{ index, (key, value) ->
                cache.userDao().updateData(Votes(index, key, value.toString().toFloat().toInt())) //update cache asynchronously
            }
            cache.close()
        }

        callback()
    }

    fun newGenreCheck(check: Boolean, name: String, callback: () -> Unit)
    {
        if(!check) // if new custom checkbox not checked
        {
            callback()
        }
        else
        {
            if(!(name.isNullOrBlank() || retrieveData.any { it.first.lowercase().contains(name.trim().lowercase()) } || name.contains('.')))
            { //check if custom option is not blank, doesnt contain each other after lower case and spaces removed, or have periods
                val cache = DataCaching.createCacheDb(context)
                val uppercaseOption = name.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase() } } //replace each word in string with first letter capital
                retrieveData.add(Pair(uppercaseOption, 1))
                CoroutineScope(Dispatchers.IO).launch()
                {
                    cache.userDao().writeData(Votes(retrieveData.size - 1, uppercaseOption, 1)) // create new record in cache
                }
                db.update(uppercaseOption, 1)
                cache.close()
                callback()
            }
            else
            {
                //need to add some form of error for nulls, blanks, and copies of previous choices
            }
        }
    }

    suspend fun clearCache()
    {
        val cache = DataCaching.createCacheDb(context)
        val votesList = cache.userDao().getData()
        votesList.forEach{ (ID, fields, votes) ->
            cache.userDao().deleteData(Votes(ID, fields, votes))
        }
        cache.close()
    }

    suspend fun getCache(): List<Votes> {
        val cache = DataCaching.createCacheDb(context)
        val votesList = cache.userDao().getData()
        cache.close()
        return votesList
    }

    fun getLDMode(): Boolean
    {
        var darkMode: Boolean
        runBlocking {
            val cache = DataCaching.createCacheDb(context)
            if(cache.userDao().getMode() == null)
            {
                cache.userDao().createMode(false)
                darkMode = false
            }
            else
            {
                darkMode = cache.userDao().getMode()
            }
            cache.close()
            if (darkMode) {
                LDmode = "Dark"
                textLDModeColor = Color.White
                primaryColor = DarkColorScheme.primary
                secondaryColor = DarkColorScheme.secondary
                tertiaryColor = DarkColorScheme.tertiary
            }
        }
        return darkMode
    }

    suspend fun updateLDMode(isDarkMode: Boolean)
    {
        val cache = DataCaching.createCacheDb(context)
        cache.userDao().updateMode(isDarkMode)
        cache.close()
    }

    suspend fun getCameraPermission(): Boolean?
    {
        val cache = DataCaching.createCacheDb(context)
        runBlocking {
            cache.userDao().updateCameraPermission((ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) //unnecessary but extra safety hurts no one
        }
        val permission = cache.userDao().getCameraPermission()
        cache.close()
        return permission
    }

    suspend fun updateCameraPermission(cameraPermission: Boolean)
    {
        val cache = DataCaching.createCacheDb(context)
        cache.userDao().updateCameraPermission(cameraPermission)
        cache.close()
    }

    suspend fun getImageRotation(): Int?
    {
        val cache = DataCaching.createCacheDb(context)
        val imageRotation = cache.userDao().getImageRotation()
        cache.close()
        return imageRotation
    }

    suspend fun updateImageRotation(imageRotation: Int)
    {
        val cache = DataCaching.createCacheDb(context)
        cache.userDao().updateImageRotation(imageRotation)
        cache.close()
    }

    fun returnImageFile(): File? {
        var imageFile by mutableStateOf(context.getFileStreamPath("image1.jpeg") )
        return imageFile
    }

    fun decodeImage(imageFile: File?): Bitmap? {
        var imageID by mutableStateOf( BitmapFactory.decodeFile(imageFile?.absolutePath) )
        return imageID
    }
}