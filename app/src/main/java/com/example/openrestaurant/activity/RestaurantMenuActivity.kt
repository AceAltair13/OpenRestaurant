package com.example.openrestaurant.activity

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.RestaurantCategoryItemClicked
import com.example.openrestaurant.adapter.RestaurantMenuAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import com.example.openrestaurant.model.Menu as RestaurantMenu

class RestaurantMenuActivity : AppCompatActivity(), RestaurantCategoryItemClicked {
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: RestaurantMenuAdapter
    private lateinit var restaurantName: String
    private lateinit var restaurantId: String
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)
//        restaurantName = intent.extras?.get("RESTAURANT_NAME").toString()
//        restaurantId = intent.extras?.get("RESTAURANT_ID").toString()
        restaurantName = Paper.book().read("RESTAURANT_NAME")
        restaurantId = Paper.book().read("RESTAURANT_ID")
        title = restaurantName
        supportActionBar?.subtitle = "Restaurant Menu"
        recyclerView = findViewById(R.id.recyclerView2)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        var categoryData = ArrayList<RestaurantMenu>()

//        if (savedInstanceState != null) {
//            restaurantId = savedInstanceState.getString("RESTAURANT_ID").toString()
//            restaurantName = savedInstanceState.getString("RESTAURANT_NAME").toString()
//        } else {
//            Toast.makeText(this, "There was an error getting savedInstance", Toast.LENGTH_LONG).show()
//        }

        db.collection("restaurants")
            .document(restaurantId).collection("menu")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    var currDoc = document.toObject(RestaurantMenu::class.java)
                    currDoc.id = document.id
                    categoryData.add(currDoc)
                }
                menuAdapter.updateRestaurantMenu(categoryData)
//                Toast.makeText(this, "Retrieved Category Items $categoryData", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->

                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RestaurantMenuActivity)
            menuAdapter = RestaurantMenuAdapter(this@RestaurantMenuActivity)
            adapter = menuAdapter
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("RESTAURANT_NAME", restaurantName)
//        outState.putString("RESTAURANT_ID", restaurantId)
//    }

    override fun onClicked(item: RestaurantMenu) {
        val intent = Intent(this, RestaurantMenuItemsActivity::class.java)
//        intent.putExtra("RESTAURANT_ID", restaurantId)
//        intent.putExtra("CATEGORY_ID", item.id)
        Paper.book().write("MENU_ID", item.id)
        startActivity(intent)
    }
}