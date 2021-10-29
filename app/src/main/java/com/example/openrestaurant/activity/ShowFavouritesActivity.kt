package com.example.openrestaurant.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.FavouriteItemOnClicked
import com.example.openrestaurant.adapter.FavouriteOrderAdapter
import com.example.openrestaurant.model.CartItem
import com.example.openrestaurant.model.Item
import com.example.openrestaurant.model.OrderItem
import com.example.openrestaurant.model.RestaurantDataGPS
import com.example.openrestaurant.paperdb.OrderCart
import com.example.openrestaurant.roomdb.Favourite
import com.example.openrestaurant.roomdb.FavouriteViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper

class ShowFavouritesActivity : AppCompatActivity(), FavouriteItemOnClicked {
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var favouriteAdapter: FavouriteOrderAdapter
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
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
                favouriteAdapter.updateFavouriteOrders(it.reversed())
                if (it.isEmpty()) {
                    findViewById<TextView>(R.id.favouriteEmptyMessage).visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onClicked(favourite: Favourite) {
        Paper.book().write("RESTAURANT_ID", favourite.restaurant_id)
        findViewById<ProgressBar>(R.id.favouriteProgressBar).visibility = View.VISIBLE
        val orderItems: ArrayList<OrderItem> = ArrayList()
        db.collection("restaurants")
            .document(favourite.restaurant_id)
            .collection("orders")
            .document(favourite.order_id)
            .get(Source.SERVER)
            .addOnSuccessListener { order ->
                val tasks: ArrayList<Task<DocumentSnapshot>> = ArrayList()
                val data = order.data?.get("items") as List<Map<String, Object>>

                db.collection("restaurants")
                    .document(favourite.restaurant_id)
                    .get(Source.SERVER)
                    .addOnSuccessListener { res ->
                        val restaurant = res.toObject(RestaurantDataGPS::class.java)
                        if (restaurant != null) {
                            Paper.book().write("RESTAURANT_NAME", restaurant.name)
                        }
                    }
                    .addOnFailureListener { ex ->
                        Toast.makeText(this, "$ex", Toast.LENGTH_SHORT).show()
                    }

                data.forEach { orderItem ->
                    val qty = orderItem["quantity"] as Long
                    val itemPath = orderItem["itemPath"] as String
                    orderItems.add(OrderItem(itemPath, qty))

                    tasks.add(db.collection("restaurants")
                        .document(itemPath).get())
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                    .addOnSuccessListener { fetchedItems ->
                        Paper.book().delete("cart")
                        Log.d("Cart Items",
                            "onClicked: ${Paper.book().read("cart", "No Items")}")

                        fetchedItems.forEachIndexed { i, fetchedItem ->
                            val doc = fetchedItem.toObject(Item::class.java)
                            if (doc != null) {
                                doc.id = fetchedItem.id
                                for (j in 1..orderItems[i].quantity!!) {
                                    OrderCart.addItem(CartItem(doc, orderItems[i].itemPath!!))
                                }
                            }
                        }
                        Log.d("FINAL CART", "onClicked: ${OrderCart.getCart()}")

                        startActivity(
                            Intent(
                                this, ConfirmOrderActivity::class.java
                            )
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.e("Error", "onClicked: $e")
                    }
            }
            .addOnFailureListener {
                Log.e("Firebase", "onClicked: ${it.message}")
            }
        findViewById<ProgressBar>(R.id.favouriteProgressBar).visibility = View.GONE
    }

    override fun onDelete(favourite: Favourite) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete '${favourite.favourite_name}' ?")
            .setPositiveButton("Delete") { dialogInterface, _ ->
                favouriteViewModel.deleteFavourite(favourite)
                Toast.makeText(this, "'${favourite.favourite_name}' Deleted!", Toast.LENGTH_SHORT)
                    .show()
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }
}