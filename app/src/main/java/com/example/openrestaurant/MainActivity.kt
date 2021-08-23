package com.example.openrestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar!!.hide()

        // TODO: Add MainActivity Logic

        findViewById<Button>(R.id.customerLoginButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ConfirmOrder::class.java,
                )
            )
        }


    }
}