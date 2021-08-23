package com.example.openrestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class ConfirmOrder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)

        findViewById<Button>(R.id.confirmIdButton).setOnClickListener {
            val confirmationID = findViewById<TextInputEditText>(R.id.confirmIdTextField).editableText.toString()
            if (confirmationID.isNotEmpty()) {
                // TODO: Remove Toast and Add Confirmation ID Logic
                Toast.makeText(this, "Order ID: $confirmationID", Toast.LENGTH_SHORT).show()
            }

        }
    }
}