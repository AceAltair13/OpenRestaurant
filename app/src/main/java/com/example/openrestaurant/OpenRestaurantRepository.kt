package com.example.openrestaurant

import androidx.lifecycle.LiveData

class OpenRestaurantRepository(private val openRestaurantDao:OpenRestaurantDao) {
    val allFavourites : LiveData<List<Favourite>> = openRestaurantDao.getAllFavourite()
    suspend fun insertFavourite(favourite:Favourite){
        openRestaurantDao.insertFavourite(favourite)
    }
    suspend fun deleteFavourite(favourite:Favourite){
        openRestaurantDao.deleteFavourite(favourite)
    }
}