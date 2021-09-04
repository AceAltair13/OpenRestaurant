package com.example.openrestaurant

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface OpenRestaurantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite:Favourite)

    @Delete
    suspend fun deleteFavourite(favourite:Favourite)

    @Query("Select * from noteS_table order by id ASC")
    fun getAllFavourite(): LiveData<List<Favourite>>
}