package com.example.openrestaurant.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.openrestaurant.R
import io.paperdb.Paper

class QRCodeActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val MY_CAMERA_PERMISSION_REQUEST = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        title = "Place Order"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        val scannerView: CodeScannerView = findViewById(R.id.qrCodeScanner)

        codeScanner = CodeScanner(this, scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = true

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if (it.text.equals(Paper.book().read("RESTAURANT_ID"))) {
                        startActivity(Intent(this@QRCodeActivity,
                            FinalOrderPlacedActivity::class.java))
                    } else {
                        Toast.makeText(this@QRCodeActivity,
                            "QR Code does NOT match the restaurant!",
                            Toast.LENGTH_LONG).show()
                        onResume()
                    }
                }
            }
            errorCallback = ErrorCallback.SUPPRESS
        }
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                MY_CAMERA_PERMISSION_REQUEST
            )
        } else {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            codeScanner.startPreview()
        } else {
            Toast.makeText(
                this,
                "QR Scanner cannot be used unless permission is provided.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}