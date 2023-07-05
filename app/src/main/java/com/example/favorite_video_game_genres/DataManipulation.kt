package com.example.favorite_video_game_genres

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.favorite_video_game_genres.ui.theme.DarkColorScheme
import com.example.favorite_video_game_genres.ui.theme.LightColorScheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DataManipulation(contextParam: Context) {
    var retrieveData = mutableListOf<Pair<String, Int>>()
    var LDmode by mutableStateOf("Light")
    var primaryColor by mutableStateOf(LightColorScheme.primary)
    var secondaryColor by mutableStateOf(LightColorScheme.secondary)
    var tertiaryColor by mutableStateOf(LightColorScheme.tertiary)
    var textLDModeColor by mutableStateOf(Color.Black)
    var fetched by mutableStateOf(false)
    var context: Context? = contextParam
    val db = FirebaseFirestore.getInstance().collection("game_counts").document("84c8g5rVr8KJliP4108c")

    fun fetchFromFireBase(callback: () -> Unit) {
        if(!fetched)
        {
            val cache = DataCaching.createCacheDb(context!!)?.userDao()
            db.get(Source.SERVER)
                .addOnSuccessListener { document ->
                    val data = document.data
                    CoroutineScope(Dispatchers.Main).launch()
                    {
                        data!!.toSortedMap().forEach { (key, value) ->
                            retrieveData.add(Pair(key, value.toString().toFloat().toInt()))
                        }
                        fetched = true
                        callback()
                        withContext(Dispatchers.IO)
                        {
                            val votesList = cache?.getData()
                            if(votesList.isNullOrEmpty())
                            {
                                retrieveData.forEachIndexed { index, (key, value) ->
                                    cache?.writeData(Votes(index, key, value.toString().toFloat().toInt()))
                                }
                            }
                            else
                            {
                                if(votesList.size != retrieveData.size)
                                {
                                    clearCache()
                                    retrieveData.forEachIndexed { index, (key, value) ->
                                        cache.writeData(Votes(index, key, value.toString().toFloat().toInt()))
                                    }
                                }
                                else
                                {
                                    retrieveData.forEachIndexed { index, (key, value) ->
                                        cache.updateData(Votes(index, key, value.toString().toFloat().toInt()))
                                    }
                                }
                                DataCaching.closeCacheDb()
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    var votesList:List<Votes>? = null
                    val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    if(connectivityManager.activeNetwork == null)
                    {
                        CoroutineScope(Dispatchers.Main).launch()
                        {
                            votesList = cache?.getData()
                            votesList!!.forEach { (_, fields, votes) ->
                                retrieveData.add(Pair(fields, votes))
                            }
                            DataCaching.closeCacheDb()
                            fetched = true
                            callback()
                        }
                    }
                    else
                    {
                        CoroutineScope(Dispatchers.IO).launch() {
                            votesList!!.forEach { (_, fields, votes) ->
                                retrieveData.add(Pair(fields, votes))
                            }
                            DataCaching.closeCacheDb()
                            fetched = true
                        }
                        callback()
                    }
                }
        }
    }

    fun updateDB(checked: MutableList<Boolean>, callback: () -> Unit)
    {
        val cache = DataCaching.createCacheDb(context!!)?.userDao()
        for((i, value) in checked.withIndex())
        {
            if(value)
            {
                retrieveData[i] = retrieveData[i].copy(second = retrieveData[i].second + 1)
                db.update(retrieveData[i].first, retrieveData[i].second)
            }
        }
        retrieveData.sortBy { it.first }
        CoroutineScope(Dispatchers.IO).launch{
            retrieveData.forEachIndexed{ index, (key, value) ->
                cache?.updateData(Votes(index, key, value.toString().toFloat().toInt()))
            }
            DataCaching.closeCacheDb()
        }

        callback()
    }

    fun newGenreCheck(check: Boolean, name: String, callback: () -> Unit)
    {
        if(!check)
        {
            callback()
        }
        else
        {
            if(!(name.isNullOrBlank() || retrieveData.any { it.first.lowercase().contains(name.trim().lowercase()) } || name.contains('.')))
            {
                val cache = DataCaching.createCacheDb(context!!)?.userDao()
                val uppercaseOption = name.split(" ").joinToString(" ") { it.replaceFirstChar { it.uppercase() } }
                retrieveData.add(Pair(uppercaseOption, 1))
                CoroutineScope(Dispatchers.IO).launch()
                {
                    cache?.writeData(Votes(retrieveData.size - 1, uppercaseOption, 1))
                }
                db.update(uppercaseOption, 1)
                DataCaching.closeCacheDb()
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
        val cache = DataCaching.createCacheDb(context!!)?.userDao()
        val votesList = cache?.getData()
        votesList?.forEach{ (ID, fields, votes) ->
            cache.deleteData(Votes(ID, fields, votes))
        }
        DataCaching.closeCacheDb()
    }

    suspend fun getCache(): List<Votes>? {
        val cache = DataCaching.createCacheDb(context!!)?.userDao()
        val votesList = cache?.getData()
        DataCaching.closeCacheDb()
        return votesList
    }

    fun getLDMode(): Boolean
    {
        var darkMode: Boolean
        runBlocking {
            val cache = DataCaching.createCacheDb(context!!)?.userDao()
            val getMode = cache?.getMode()
            if(getMode == null)
            {
                cache?.createMode(LDMode(0, false))
                DataCaching.closeCacheDb()
                darkMode = false
            }
            else
            {
                DataCaching.closeCacheDb()
                darkMode = getMode
                if (darkMode) {
                    LDmode = "Dark"
                    textLDModeColor = Color.White
                    primaryColor = DarkColorScheme.primary
                    secondaryColor = DarkColorScheme.secondary
                    tertiaryColor = DarkColorScheme.tertiary
                }
            }
        }
        return darkMode
    }

    suspend fun updateLDMode(isDarkMode: Boolean)
    {
        val cache = DataCaching.createCacheDb(context!!)?.userDao()
        cache?.updateMode(LDMode(0, isDarkMode))
        DataCaching.closeCacheDb()
    }
}