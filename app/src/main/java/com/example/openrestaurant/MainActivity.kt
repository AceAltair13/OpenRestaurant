package com.example.openrestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private val userName = "tirth@email.com"
    private val passWord = "pass1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar!!.hide()

        findViewById<Button>(R.id.customerLoginButton).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    CustomerHomeActivity::class.java,
                )
            )
        }

        findViewById<Button>(R.id.businessLoginButton).setOnClickListener {
            Toast.makeText(this, "Feature Coming Soon!", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnCreateBusiness).setOnClickListener {
            Toast.makeText(this, "Feature Coming Soon!", Toast.LENGTH_SHORT).show()
        }

    }
}