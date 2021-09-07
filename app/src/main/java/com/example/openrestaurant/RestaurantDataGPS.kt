package com.example.openrestaurant

import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

data class RestaurantDataGPS(
    var name: String? = "",
    var location: GeoPoint? = null,
    var menu: ArrayList<Menu>? = null,
    var distance: Int? = 0
)

data class Menu(
    var category: String? = "",
    var items: ArrayList<Item>? = null
) : Serializable

data class Item(
    var item_name: String? = "",
    var item_price: Int? = 0,
    var image: String? = ""
) : Serializable