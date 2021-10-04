package com.example.openrestaurant.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.openrestaurant.roomdb.Favourite
import com.example.openrestaurant.roomdb.OpenRestaurantDatabase
import com.example.openrestaurant.roomdb.OpenRestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application): AndroidViewModel(application) {

    private val repository: OpenRestaurantRepository
    val allFavourites: LiveData<List<Favourite>>

    init {
        val dao = OpenRestaurantDatabase.getDatabase(application).getOpenRestaurantDao()
        repository = OpenRestaurantRepository(dao)
        allFavourites = repository.allFavourites
    }

    fun deleteFavourite(favourite: Favourite) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFavourite(favourite)
    }
    fun insertFavourite(favourite: Favourite) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertFavourite(favourite)
    }

}