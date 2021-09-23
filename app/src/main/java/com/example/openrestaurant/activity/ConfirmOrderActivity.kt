package com.example.openrestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.RestaurantFinalItemsAdapter
import com.example.openrestaurant.paperdb.OrderCart
import com.google.android.material.textfield.TextInputEditText

class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var finalItemAdapter: RestaurantFinalItemsAdapter
    private val cartItems = OrderCart.getCart()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        title = "Confirm Order"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        recyclerView = findViewById(R.id.recyclerView4)

        findViewById<TextView>(R.id.finalItemTotalCost).text = "₹ ${OrderCart.getTotalCost()}"

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConfirmOrderActivity)
            finalItemAdapter = RestaurantFinalItemsAdapter()
            finalItemAdapter.updateFinalItems(cartItems)
            adapter = finalItemAdapter
        }

        findViewById<Button>(R.id.btnPlaceOrder).setOnClickListener {
            startActivity(Intent(this, FinalOrderPlacedActivity::class.java))
        }

    }
}