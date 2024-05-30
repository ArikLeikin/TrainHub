package com.example.trainhub.models.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.trainhub.TrainHubApplication
import com.example.trainhub.models.entities.Post
import com.example.trainhub.models.entities.User

@Database(entities = [User::class,Post::class], version = 1)
abstract class AppLocalDbRepository : RoomDatabase(){
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
}

object AppLocalDatabase {

    val db: AppLocalDbRepository by lazy {

        val context = TrainHubApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "mainDB.db"
        )
            .fallbackToDestructiveMigration() //NEED TO check if needed
            .build()
    }
}