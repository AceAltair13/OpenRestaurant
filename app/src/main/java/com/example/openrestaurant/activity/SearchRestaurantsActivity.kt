package com.example.openrestaurant.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.adapter.RestaurantDataGPSAdapter
import com.example.openrestaurant.adapter.RestaurantDataGPSItemClicked
import com.example.openrestaurant.model.RestaurantDataGPS
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.nlopez.smartlocation.SmartLocation
import io.paperdb.Paper
import java.util.*
import kotlin.collections.ArrayList


class SearchRestaurantsActivity : AppCompatActivity(), RestaurantDataGPSItemClicked,
    EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private lateinit var mAdapter: RestaurantDataGPSAdapter
    private val db = Firebase.firestore
    private val MAX_DISTANCE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_restaurants)
        supportActionBar?.elevation = 0f
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        title = ""
        getRestaurantsFromGPS()
        findViewById<FloatingActionButton>(R.id.btnSearchRefresh).setOnClickListener {
            getRestaurantsFromGPS()
        }
    }

    private fun getRestaurantsFromGPS() {
        findViewById<ProgressBar>(R.id.searchProgressBar).visibility =
            View.VISIBLE
        if (hasLocationPermission()) {
            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                findViewById<ProgressBar>(R.id.searchProgressBar).visibility =
                    View.GONE
                findViewById<RecyclerView>(R.id.searchRecyclerView).visibility = View.GONE
                findViewById<TextView>(R.id.searchGpsOffErrorMsg).visibility = View.VISIBLE
                findViewById<TextView>(R.id.searchCityName).text = "Location Disabled"
            } else {
                findViewById<TextView>(R.id.searchGpsOffErrorMsg).visibility = View.GONE
                SmartLocation.with(this).location().oneFix().start { location ->
                    Log.d("GPS Message", "getRestaurantsFromGPS: $location")
                    val geoCoder = Geocoder(this)
                    val currentLocation =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                    val myLat = currentLocation.first().latitude
                    val myLong = currentLocation.first().longitude
                    findViewById<TextView>(R.id.searchCityName).text =
                        currentLocation.first().locality

                    // Firebase Retrieval
                    val restaurantData = ArrayList<RestaurantDataGPS>()
                    db.collection("restaurants")
                        .get(Source.SERVER)
                        .addOnSuccessListener { result ->
                            findViewById<ProgressBar>(R.id.searchProgressBar).visibility =
                                View.GONE
                            findViewById<RecyclerView>(R.id.searchRecyclerView).visibility =
                                View.VISIBLE
                            for (document in result) {
                                val currDoc =
                                    document.toObject(RestaurantDataGPS::class.java)
                                val results = FloatArray(1)
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
                                val distance = results[0].toInt()
                                if (distance <= MAX_DISTANCE) {
                                    currDoc.distance = distance
                                    currDoc.id = document.id
                                    restaurantData.add(currDoc)
                                }
                            }
                            restaurantData.sortBy { it.distance }
                            mAdapter.updateRestaurantDataGPS(restaurantData)
                        }
                        .addOnFailureListener { exception ->
                            findViewById<ProgressBar>(R.id.searchProgressBar).visibility =
                                View.GONE
                            Log.w("Firebase Warning", "Error getting documents. $exception")
                            Toast.makeText(this@SearchRestaurantsActivity,
                                "Error retrieving data! Maybe check your internet?",
                                Toast.LENGTH_SHORT)
                                .show()
                        }

                    findViewById<RecyclerView>(R.id.searchRecyclerView).apply {
                        layoutManager =
                            LinearLayoutManager(this@SearchRestaurantsActivity)
                        mAdapter =
                            RestaurantDataGPSAdapter(this@SearchRestaurantsActivity,
                                myLat,
                                myLong)
                        adapter = mAdapter
                    }
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    override fun onClicked(item: RestaurantDataGPS) {
        val intent = Intent(
            this,
            RestaurantMenuActivity::class.java
        )
        Paper.book().delete("cart")
        Paper.book().write("RESTAURANT_NAME", item.name)
        Paper.book().write("RESTAURANT_ID", item.id)
        startActivity(intent)
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
