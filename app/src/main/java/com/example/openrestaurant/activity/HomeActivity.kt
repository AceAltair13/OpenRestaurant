package com.example.openrestaurant.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.openrestaurant.R
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {
    private lateinit var btnSearchRestaurants: MaterialCardView
    private lateinit var btnShowFavourites: MaterialCardView
    private lateinit var btnAboutUs: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar!!.hide()
        btnSearchRestaurants = findViewById(R.id.btnSearchRestaurants)
        btnShowFavourites = findViewById(R.id.btnShowFavourites)
        btnAboutUs = findViewById(R.id.btnAboutUs)

        btnSearchRestaurants.setOnClickListener {
            startActivity(Intent(this, SearchRestaurantsActivity::class.java))
        }

        btnShowFavourites.setOnClickListener {
            startActivity(Intent(this, ShowFavouritesActivity::class.java))
        }

        btnAboutUs.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

    }
}