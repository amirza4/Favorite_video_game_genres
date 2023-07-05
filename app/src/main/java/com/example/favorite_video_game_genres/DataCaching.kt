package com.example.favorite_video_game_genres

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class Votes(
    @PrimaryKey val ID: Int,
    val fields: String,
    var votes: Int
)

@Entity
data class LDMode(
    @PrimaryKey val ID: Int,
    var lDMode: Boolean
)

@Dao
interface UserDao
{
    @Query("SELECT * FROM Votes")
    suspend fun getData(): List<Votes>

    @Insert
    suspend fun writeData(Votes: Votes)

    @Update
    suspend fun updateData(Votes: Votes)

    @Delete
    suspend fun deleteData(Votes: Votes)

    @Query("SELECT lDmode FROM LDMode WHERE ID=0")
    suspend fun getMode(): Boolean

    @Insert
    suspend fun createMode(LDMode: LDMode)

    @Update
    suspend fun updateMode(LDMode: LDMode)
}

@Database([Votes::class, LDMode::class], version = 1, exportSchema = false)
abstract class DataCaching : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        var dbOpen: Boolean = false
        var Db: DataCaching? = null

        fun createCacheDb(context: Context): DataCaching? {
            if (!dbOpen)
            {
                dbOpen = true
                Db = Room.databaseBuilder(context.applicationContext, DataCaching::class.java, "database1").build()
                return Db
            } else
            {
                return null
            }
        }

        fun closeCacheDb() {
            Db?.close()
            Db = null
            dbOpen = false
        }
    }
}