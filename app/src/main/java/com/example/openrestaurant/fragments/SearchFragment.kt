package com.example.openrestaurant.fragments

import android.content.Intent
import android.location.Location
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
import com.example.openrestaurant.activity.RestaurantMenuActivity
import com.example.openrestaurant.adapter.RestaurantDataGPSAdapter
import com.example.openrestaurant.adapter.RestaurantDataGPSItemClicked
import com.example.openrestaurant.model.RestaurantDataGPS
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper

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
        val latitude = Paper.book().read<Double>("LATITUDE")
        val longitude = Paper.book().read<Double>("LONGITUDE")

        db.collection("restaurants")
            .get()
            .addOnSuccessListener { result ->
                view.findViewById<ProgressBar>(R.id.progressBarNearbyRestaurants).visibility =
                    View.GONE
                for (document in result) {
                    var currDoc = document.toObject(RestaurantDataGPS::class.java)
                    var results = FloatArray(1)
                    // TODO: Add actual location referencing
                    currDoc.location?.latitude?.let {
                        currDoc.location?.longitude?.let { it1 ->
                            Location.distanceBetween(
                                it,
                                it1, latitude, longitude, results
                            )
                        }
                    }
                    Log.d("Curr Lat", "$latitude")
                    Log.d("Curr Long", "$longitude")
                    currDoc.distance = results[0].toInt()
                    currDoc.id = document.id
                    restaurantData.add(currDoc)
                }
                restaurantData.sortBy { it.distance }
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
            mAdapter = RestaurantDataGPSAdapter(this@SearchFragment, 0.0, 0.0)
            findViewById<RecyclerView>(R.id.recyclerView).adapter = mAdapter
        }
    }

    override fun onClicked(item: RestaurantDataGPS) {
        val intent = Intent(
            context,
            RestaurantMenuActivity::class.java
        )
//        intent.putExtra("RESTAURANT_NAME", item.name)
//        intent.putExtra("RESTAURANT_ID", item.id)
        Paper.book().write("RESTAURANT_NAME", item.name)
        Paper.book().write("RESTAURANT_ID", item.id)
        startActivity(intent)
    }

}