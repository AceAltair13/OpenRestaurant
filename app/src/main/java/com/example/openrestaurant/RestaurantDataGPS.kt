package com.example.openrestaurant

import com.google.firebase.firestore.GeoPoint

data class RestaurantDataGPS(
    var name: String? = "",
    var location: GeoPoint? = null,
    var menu: List<Menu>? = null,
    var distance: Int? = 0
)

data class Menu(
    var category: String? = "",
    var items: List<Item>? = null
)

data class Item(
    var itemName: String? = "",
    var itemPrice: Int? = 0,
    var image: String? = ""
)