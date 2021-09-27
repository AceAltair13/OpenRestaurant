package com.example.openrestaurant.activity

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.RestaurantCategoryItemClicked
import com.example.openrestaurant.adapter.RestaurantMenuAdapter
import com.example.openrestaurant.paperdb.OrderCart
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
        restaurantName = Paper.book().read("RESTAURANT_NAME")
        restaurantId = Paper.book().read("RESTAURANT_ID")
        title = restaurantName
        supportActionBar?.subtitle = "Restaurant Menu"
        recyclerView = findViewById(R.id.recyclerView2)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        findViewById<Button>(R.id.btnMenuProceed).isEnabled = OrderCart.getOrderCartSize() != 0

        var categoryData = ArrayList<RestaurantMenu>()

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

        findViewById<Button>(R.id.btnMenuProceed).setOnClickListener {
            val intent = Intent(this, ConfirmOrderActivity::class.java)
            startActivity(intent)


//            val cartItem = OrderCart.getCart()[0]
//            val docPath = cartItem.itemPath
//
//            db.collection("restaurants")
//                .document(docPath)
//                .get()
//                .addOnSuccessListener { result ->
//                    Log.d("Fetched Item", "${result.data}")
//                }
//                .addOnFailureListener {
//                    Log.e("Error", "Error fetching document")
//                }

        }

    }

    override fun onClicked(item: RestaurantMenu) {
        val intent = Intent(this, RestaurantMenuItemsActivity::class.java)
        Paper.book().write("MENU_ID", item.id)
        Paper.book().write("MENU_NAME", item.category)
        startActivity(intent)
    }
}