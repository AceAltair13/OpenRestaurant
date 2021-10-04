package com.example.openrestaurant.roomdb

import androidx.lifecycle.LiveData
import com.example.openrestaurant.roomdb.Favourite
import com.example.openrestaurant.roomdb.OpenRestaurantDao

class OpenRestaurantRepository(private val openRestaurantDao: OpenRestaurantDao) {

    val allFavourites: LiveData<List<Favourite>> = openRestaurantDao.getAllFavourite()

    suspend fun insertFavourite(favourite: Favourite) {
        openRestaurantDao.insertFavourite(favourite)
    }

    suspend fun deleteFavourite(favourite: Favourite) {
        openRestaurantDao.deleteFavourite(favourite)
    }
}