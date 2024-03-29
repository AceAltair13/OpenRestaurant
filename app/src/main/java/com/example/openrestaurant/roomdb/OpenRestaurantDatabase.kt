package com.example.openrestaurant.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Favourite::class], version = 2, exportSchema = false)
abstract class OpenRestaurantDatabase : RoomDatabase() {
    abstract fun getOpenRestaurantDao(): OpenRestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: OpenRestaurantDatabase? = null
        fun getDatabase(context: Context): OpenRestaurantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OpenRestaurantDatabase::class.java,
                    "open_restaurant_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}