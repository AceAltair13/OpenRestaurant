package com.example.openrestaurant.activity

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.openrestaurant.R
import com.example.openrestaurant.paperdb.OrderCart
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class FinalOrderPlacedActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val cart = OrderCart.getCart()
    private lateinit var successImage: ImageView
    private lateinit var successTxt: TextView
    private lateinit var successProgressBar: ProgressBar
    private lateinit var successProgressTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_order_placed)
        supportActionBar!!.hide()
        successImage = findViewById(R.id.successImage)
        successTxt = findViewById(R.id.successMsg)
        successProgressBar = findViewById(R.id.successLoadingBar)
        successProgressTxt = findViewById(R.id.successLoadingMsg)

        val cartItems = HashMap<String, Any>()
        val cartList = ArrayList<HashMap<String, Any>>()

        cart.forEach {
            var cartListItem = HashMap<String, Any>()
            cartListItem["itemPath"] = it.itemPath
            cartListItem["quantity"] = it.quantity
            cartList.add(cartListItem)
        }
        cartItems["items"] = cartList

        db.collection("orders")
            .add(cartItems)
            .addOnSuccessListener {
                successProgressBar.visibility = View.GONE
                successProgressTxt.visibility = View.GONE
                successImage.visibility = View.VISIBLE
                successTxt.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Log.e("Order Placing", it.toString())
            }

    }
}