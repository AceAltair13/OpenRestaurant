package com.example.openrestaurant.activity

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.marginStart
import androidx.lifecycle.ViewModelProvider
import com.example.openrestaurant.MainActivity
import com.example.openrestaurant.R
import com.example.openrestaurant.model.Favourite
import com.example.openrestaurant.paperdb.OrderCart
import com.example.openrestaurant.roomdb.FavouriteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import org.w3c.dom.Text
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FinalOrderPlacedActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val cart = OrderCart.getCart()
    private lateinit var favouriteOrderId: String
    private lateinit var successCardContents: ConstraintLayout
    private lateinit var loadingCardContents: ConstraintLayout
    private lateinit var failureCardContents: ConstraintLayout
    private lateinit var btnFavourite: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_order_placed)
        supportActionBar!!.hide()

        successCardContents = findViewById(R.id.successCardContents)
        loadingCardContents = findViewById(R.id.loadingCardContents)
        failureCardContents = findViewById(R.id.failureCardContents)
        btnFavourite = findViewById(R.id.btnFavourite)

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
                loadingCardContents.visibility = View.GONE
                favouriteOrderId = it.id
                successCardContents.findViewById<TextView>(R.id.successFavouriteOrderId).text =
                    "Order ID : $favouriteOrderId"

                successCardContents.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                loadingCardContents.visibility = View.GONE
                failureCardContents.visibility = View.VISIBLE
                Log.e("Order Placing", it.toString())
            }

        btnFavourite.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val input = LayoutInflater.from(this).inflate(R.layout.dialog_favourite, null)
        input.findViewById<TextView>(R.id.favouriteOrderId).text = "Order ID: $favouriteOrderId"
        MaterialAlertDialogBuilder(this)
            .setTitle("Add this Order to Favourites")
            .setView(input)
            .setPositiveButton("Save", DialogInterface.OnClickListener { dialogInterface, i ->
                val favouriteLabel =
                    input.findViewById<TextInputEditText>(R.id.favouriteNicknameText).text.toString()
                if (favouriteLabel.isNotEmpty()) {
                    val favouriteList: ArrayList<Favourite> =
                        Paper.book().read("favourites", ArrayList())
                    favouriteList.add(Favourite(favouriteOrderId, favouriteLabel))
                    Paper.book().write("favourites", favouriteList)
                    Toast.makeText(this,
                        "$favouriteLabel added to favourites!",
                        Toast.LENGTH_LONG)
                        .show()
                    btnFavourite.text = "Added To Favourites"
                    btnFavourite.isEnabled = false
                    dialogInterface.dismiss()
                } else {
                    Toast.makeText(this, "Label cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
            .show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}