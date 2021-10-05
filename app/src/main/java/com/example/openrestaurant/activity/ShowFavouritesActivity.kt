package com.example.openrestaurant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.FavouriteItemOnClicked
import com.example.openrestaurant.adapter.FavouriteOrderAdapter
import com.example.openrestaurant.roomdb.Favourite
import com.example.openrestaurant.roomdb.FavouriteViewModel

class ShowFavouritesActivity : AppCompatActivity(), FavouriteItemOnClicked {
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var favouriteAdapter: FavouriteOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_favourites)
        supportActionBar?.elevation = 0f
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        recyclerView = findViewById(R.id.favouriteRecyclerView)
        findViewById<TextView>(R.id.favouriteEmptyMessage).visibility = View.GONE
        findViewById<ProgressBar>(R.id.favouriteProgressBar).visibility = View.VISIBLE

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ShowFavouritesActivity)
            favouriteAdapter = FavouriteOrderAdapter(this@ShowFavouritesActivity)
            adapter = favouriteAdapter
        }

        favouriteViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            FavouriteViewModel::class.java)
        favouriteViewModel.allFavourites.observe(this, {
            it?.let {
                findViewById<ProgressBar>(R.id.favouriteProgressBar).visibility = View.GONE
                favouriteAdapter.updateFavouriteOrders(it)
                if (it.isEmpty()) {
                    findViewById<TextView>(R.id.favouriteEmptyMessage).visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onClicked(favourite: Favourite) {
        Toast.makeText(this, "Favourite Tapped!", Toast.LENGTH_SHORT).show()
    }

    override fun onDelete(favourite: Favourite) {
        favouriteViewModel.deleteFavourite(favourite)
        Toast.makeText(this, "Favourite Deleted!", Toast.LENGTH_SHORT).show()
    }
}