package com.example.perpustakaan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Series::class], version = 1)
@TypeConverters(SeriesTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSeriesDao(): SeriesDao

    companion object {

        // Function to build the app database on app initialization
        fun buildAppDatabase(appContext: Context): AppDatabase = Room.databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            name = "perpustakaan-db"
        ).build()

    }

}