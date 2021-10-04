package com.example.openrestaurant.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.openrestaurant.roomdb.Favourite


@Dao
interface OpenRestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite: Favourite)

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)

    @Query("SELECT * FROM favourite")
    fun getAllFavourite(): LiveData<List<Favourite>>
}