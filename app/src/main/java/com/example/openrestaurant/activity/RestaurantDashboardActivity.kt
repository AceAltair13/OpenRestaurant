package com.example.openrestaurant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.openrestaurant.R

class RestaurantDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_dashboard)
        this.supportActionBar!!.hide()
    }
}