package com.example.favorite_video_game_genres.data

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
    var votes: Int //these are the tables and columns
)

@Entity
data class Setting(
    @PrimaryKey val ID: Int,
    var lDMode: Boolean?,
    var cameraPermission: Boolean?,
    var imageRotation: Int?
)

@Dao
interface UserDao // the sql lite commands linked to specific tables
{
    @Query("SELECT * FROM Votes")
    suspend fun getData(): List<Votes>

    @Insert
    suspend fun writeData(Votes: Votes)

    @Update
    suspend fun updateData(Votes: Votes)

    @Delete
    suspend fun deleteData(Votes: Votes)

    @Query("SELECT lDmode FROM Setting WHERE ID=0")
    suspend fun getMode(): Boolean?

    @Query("INSERT INTO Setting (ID, lDMode, cameraPermission, imageRotation) Values (0, :LDMode, :CameraPermission, :ImageRotation)")
    suspend fun createSetting(LDMode: Boolean?, CameraPermission: Boolean?, ImageRotation: Int?)

    @Query("UPDATE Setting SET lDMode = :LDMode WHERE ID = 0")
    suspend fun updateMode(LDMode: Boolean)

    @Query("SELECT cameraPermission FROM Setting WHERE ID = 0")
    suspend fun getCameraPermission(): Boolean?

    @Query("UPDATE Setting SET cameraPermission = :cameraPermission WHERE ID = 0")
    suspend fun updateCameraPermission(cameraPermission: Boolean)

    @Query("SELECT imageRotation FROM Setting where ID = 0")
    suspend fun getImageRotation(): Int?

    @Query("UPDATE Setting SET imageRotation = :imageRotation WHERE ID = 0")
    suspend fun updateImageRotation(imageRotation: Int)
}

@Database([Votes::class, Setting::class], version = 5, exportSchema = false)
abstract class DataCaching : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        lateinit var Db: DataCaching

        fun createCacheDb(context: Context): DataCaching {
            Db = Room.databaseBuilder(context.applicationContext, DataCaching::class.java, "database1")
                .fallbackToDestructiveMigration()
                .build()
            return Db
        }
    }
}