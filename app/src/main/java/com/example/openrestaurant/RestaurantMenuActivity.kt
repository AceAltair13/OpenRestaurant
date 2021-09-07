package com.example.openrestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.Menu as RestaurantMenu

class RestaurantMenuActivity : AppCompatActivity(), RestaurantCategoryItemClicked {
    private lateinit var restaurantMenu: ArrayList<RestaurantMenu>
    private lateinit var menuAdapter: RestaurantMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)
        title = intent.extras?.get("RESTAURANT_NAME").toString()
        supportActionBar?.subtitle = "Restaurant Menu"

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        restaurantMenu = intent.extras?.get("RESTAURANT_MENU") as ArrayList<RestaurantMenu>
        findViewById<RecyclerView>(R.id.recyclerView2).layoutManager = LinearLayoutManager(this)
        menuAdapter = RestaurantMenuAdapter(this)
        menuAdapter.updateRestaurantMenu(restaurantMenu)
        findViewById<RecyclerView>(R.id.recyclerView2).adapter = menuAdapter
    }

    override fun onClicked(item: RestaurantMenu) {
        Toast.makeText(this, item.category, Toast.LENGTH_SHORT).show()
    }
}