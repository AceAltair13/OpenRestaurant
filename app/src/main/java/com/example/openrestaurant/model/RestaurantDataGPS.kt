package com.example.openrestaurant.model

import com.google.firebase.firestore.GeoPoint

data class RestaurantDataGPS(
    var id: String? = "",
    var name: String? = "",
    var location: GeoPoint? = null,
    var distance: Int? = 0
)