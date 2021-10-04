package com.example.openrestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.RestaurantItemAdapter
import com.example.openrestaurant.model.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper

class RestaurantMenuItemsActivity : AppCompatActivity() {
    private lateinit var itemAdapter: RestaurantItemAdapter
    private lateinit var categoryId: String
    private lateinit var restaurantId: String
    private val db = Firebase.firestore
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu_items)
        val menuName: String = Paper.book().read("MENU_NAME")
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        title = ""
        supportActionBar?.elevation = 0f
        categoryId = Paper.book().read("MENU_ID")
        restaurantId = Paper.book().read("RESTAURANT_ID")
        recyclerView = findViewById(R.id.recyclerView3)
        progressBar = findViewById(R.id.progressBar3)
        var items = ArrayList<Item>()

        db.collection("restaurants")
            .document(restaurantId).collection("menu")
            .document(categoryId).collection("items")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    progressBar.visibility = View.GONE
                    var currDoc = document.toObject(Item::class.java)
                    currDoc.id = document.id
                    items.add(currDoc)
                }
                itemAdapter.updateRestaurantItem(items)
            }
            .addOnFailureListener { exception ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "$exception", Toast.LENGTH_LONG).show()
            }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RestaurantMenuItemsActivity)
            itemAdapter = RestaurantItemAdapter()
            adapter = itemAdapter
        }
    }
}