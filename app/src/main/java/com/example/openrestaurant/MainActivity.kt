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
                    CustomerHome::class.java,
                )
            )
        }

        findViewById<Button>(R.id.businessLoginButton).setOnClickListener {
            val user = findViewById<TextInputEditText>(R.id.emailTextField).text.toString()
            val pass = findViewById<TextInputEditText>(R.id.passwordTextField).text.toString()

            if (user == userName && pass == passWord) {
                startActivity(
                    Intent(
                        this,
                        RestaurantDashboard::class.java
                    )
                )
            } else if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                Toast.makeText(this, "Username or Password cannot be empty!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Username or Password is Incorrect!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}