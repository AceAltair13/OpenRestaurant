package com.example.openrestaurant.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.openrestaurant.GPSutility
import com.example.openrestaurant.R
import com.google.android.gms.location.*
import io.paperdb.Paper
import android.location.Geocoder
import java.util.*


class SearchRestaurantsActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_restaurants)
        supportActionBar?.elevation = 0f
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                54)
        }
        getLocation()


    }

    private fun getLocation() {
        var myLat: Double
        var myLong: Double
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val geocoder = Geocoder(this, Locale.getDefault())
        GPSutility(this).turnOnGPS()
        val request = LocationRequest()
        request.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {
                        myLat = location.longitude
                        myLong = location.latitude
                        val addresses: List<Address> = geocoder.getFromLocation(myLat, myLong, 1)
                        val cityName = addresses[0].getAddressLine(0)
                        val stateName = addresses[0].getAddressLine(1)
                        val countryName = addresses[0].getAddressLine(2)
                        Log.d("longitude ", location.longitude.toString())
                        Log.d("latitude", location.latitude.toString())
                        Log.d("speed", location.speed.toString())
                        Log.d("altitude", location.altitude.toString())
                        Log.d("verticalAccuracyMeters", location.verticalAccuracyMeters.toString())
                    }
                }
            }, null)
        }

    }
}
