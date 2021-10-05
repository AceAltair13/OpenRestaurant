package com.example.openrestaurant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.openrestaurant.activity.CustomerHomeActivity
import com.example.openrestaurant.activity.HomeActivity
import com.example.openrestaurant.activity.QRCodeActivity
import com.google.android.gms.location.*
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar!!.hide()
        Paper.init(this)

        findViewById<Button>(R.id.customerLoginButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
        }

    }


}