package com.example.openrestaurant.model

import com.google.firebase.firestore.GeoPoint

data class RestaurantDataGPS(
    var name: String? = "",
    var location: GeoPoint? = null,
    var menu: ArrayList<Menu>? = ArrayList(),
    var distance: Int? = 0
)