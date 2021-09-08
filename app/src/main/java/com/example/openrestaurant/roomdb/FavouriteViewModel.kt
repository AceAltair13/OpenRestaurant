//package com.example.openrestaurant
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//
//class FavouriteViewModel(private val repository: FavouriteRepository) : ViewModel() {
//
//    // Using LiveData and caching what allFavourites returns has several benefits:
//    // - We can put an observer on the data (instead of polling for changes) and only update the
//    //   the UI when the data actually changes.
//    // - Repository is completely separated from the UI through the ViewModel.
//    val allFavourites: LiveData<List<Favourite>> = repository.allFavourites
//
//    /**
//     * Launching a new coroutine to insert the data in a non-blocking way
//     */
//    fun insert(favourite: Favourite) = viewModelScope.launch {
//        repository.insert(favourite)
//    }
//}
//
//class FavouriteViewModelFactory(private val repository: FavouriteRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return FavouriteViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}


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
    private val allFavourites: LiveData<List<Favourite>>

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