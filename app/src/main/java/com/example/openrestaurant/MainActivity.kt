package com.example.openrestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.openrestaurant.activity.CustomerHomeActivity
import com.example.openrestaurant.activity.QRCodeActivity

class MainActivity : AppCompatActivity() {
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
//            Toast.makeText(this, "Feature Coming Soon!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, QRCodeActivity::class.java))
        }
        findViewById<Button>(R.id.btnCreateBusiness).setOnClickListener {
            Toast.makeText(this, "Feature Coming Soon!", Toast.LENGTH_SHORT).show()
        }

    }
}