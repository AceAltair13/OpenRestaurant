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

    @Query("Select * from favourite order by timestamp DESC")
    fun getAllFavourite(): LiveData<List<Favourite>>
}