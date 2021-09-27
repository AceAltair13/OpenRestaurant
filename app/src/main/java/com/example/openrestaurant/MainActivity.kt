package com.example.openrestaurant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.openrestaurant.activity.CustomerHomeActivity
import com.example.openrestaurant.activity.QRCodeActivity
import com.google.android.gms.location.*
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    private val TAG="Main_ACTIVITY"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar!!.hide()
        Paper.init(this)

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

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),54)
        }







        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        GPSutility(this).turnOnGPS()
        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {

                        Paper.book().write("LATITUDE", location.latitude.toDouble());
                        Paper.book().write("LONGITUDE", location.longitude.toDouble());


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== Activity.RESULT_OK){
            if(requestCode==Iconstant.DEFAULTS.GPS_CODE){
                Log.d(TAG,"OnActivityResult: SUCCESS")
            }
            else{
                GPSutility(this).turnOnGPS()
            }
        }
        else{
            GPSutility(this).turnOnGPS()
        }
    }






}