package com.example.openrestaurant.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class SearchFragment : Fragment(), RestaurantDataGPSItemClicked {

    private lateinit var mAdapter: RestaurantDataGPSAdapter
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var restaurantData = ArrayList<RestaurantDataGPS>()

        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->
                view.findViewById<ProgressBar>(R.id.progressBarNearbyRestaurants).visibility =
                    View.GONE
                for (document in result) {
                    restaurantData.add(document.toObject(RestaurantDataGPS::class.java))
                }
                mAdapter.updateRestaurantDataGPS(restaurantData)
            }
            .addOnFailureListener { exception ->
                view.findViewById<ProgressBar>(R.id.progressBarNearbyRestaurants).visibility =
                    View.GONE
                Log.w("Firebase Warning", "Error getting documents. $exception")
                Toast.makeText(context, "Error retrieving data!", Toast.LENGTH_SHORT).show()
            }

        view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            findViewById<RecyclerView>(R.id.recyclerView).layoutManager =
                LinearLayoutManager(activity)
            mAdapter = RestaurantDataGPSAdapter(this@SearchFragment)
            findViewById<RecyclerView>(R.id.recyclerView).adapter = mAdapter
        }
    }

    override fun onClicked(item: RestaurantDataGPS) {
//        Toast.makeText(context, "Restaurant Name: ${item.name}", Toast.LENGTH_SHORT).show()
        val intent = Intent(
            context,
            RestaurantMenuActivity::class.java
        )
        intent.putExtra("RESTAURANT_MENU", item.menu as Serializable)
        intent.putExtra("RESTAURANT_NAME", item.name)
        startActivity(intent)
    }

}