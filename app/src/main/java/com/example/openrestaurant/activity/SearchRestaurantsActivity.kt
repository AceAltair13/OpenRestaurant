package com.example.openrestaurant.activity

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.openrestaurant.R
import com.google.android.gms.location.*
import android.location.Geocoder
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.adapter.RestaurantDataGPSAdapter
import com.example.openrestaurant.adapter.RestaurantDataGPSItemClicked
import com.example.openrestaurant.model.RestaurantDataGPS
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.*
import kotlin.collections.ArrayList


class SearchRestaurantsActivity : AppCompatActivity(), RestaurantDataGPSItemClicked,
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mAdapter: RestaurantDataGPSAdapter
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_restaurants)
        supportActionBar?.elevation = 0f
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getRestaurantsFromGPS()

        findViewById<FloatingActionButton>(R.id.btnSearchRefresh).setOnClickListener {
            getRestaurantsFromGPS()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getRestaurantsFromGPS() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                val geoCoder = Geocoder(this)
                val currentLocation = geoCoder.getFromLocation(it.latitude, it.longitude, 1)
                val myLat = currentLocation.first().latitude
                val myLong = currentLocation.first().longitude
                findViewById<TextView>(R.id.searchCityName).text = currentLocation.first().locality

                // Firebase Retrieval
                var restaurantData = ArrayList<RestaurantDataGPS>()
                db.collection("restaurants")
                    .get()
                    .addOnSuccessListener { result ->
                        findViewById<ProgressBar>(R.id.searchProgressBar).visibility =
                            View.GONE
                        for (document in result) {
                            var currDoc = document.toObject(RestaurantDataGPS::class.java)
                            var results = FloatArray(1)
                            currDoc.location?.latitude?.let {
                                currDoc.location?.longitude?.let { it1 ->
                                    Location.distanceBetween(
                                        it,
                                        it1, myLat, myLong, results
                                    )
                                }
                            }
                            Log.d("Curr Lat", "$myLat")
                            Log.d("Curr Long", "$myLong")
                            currDoc.distance = results[0].toInt()
                            currDoc.id = document.id
                            restaurantData.add(currDoc)
                        }
                        restaurantData.sortBy { it.distance }
                        mAdapter.updateRestaurantDataGPS(restaurantData)
                    }
                    .addOnFailureListener { exception ->
                        findViewById<ProgressBar>(R.id.searchProgressBar).visibility =
                            View.GONE
                        Log.w("Firebase Warning", "Error getting documents. $exception")
                        Toast.makeText(this@SearchRestaurantsActivity,
                            "Error retrieving data!",
                            Toast.LENGTH_SHORT)
                            .show()
                    }

                findViewById<RecyclerView>(R.id.searchRecyclerView).apply {
                    layoutManager =
                        LinearLayoutManager(this@SearchRestaurantsActivity)
                    mAdapter =
                        RestaurantDataGPSAdapter(this@SearchRestaurantsActivity, myLat, myLong)
                    adapter = mAdapter
                }

            }
        } else {
            requestLocationPermission()
        }
    }

    override fun onClicked(item: RestaurantDataGPS) {
        Toast.makeText(this, "${item.name}", Toast.LENGTH_SHORT).show()
    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "To search nearby restaurants, Location Permission is required.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            this,
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
        getRestaurantsFromGPS()
    }
}
