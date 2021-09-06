package com.example.openrestaurant

data class RestaurantDataGPS (
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val menu: List<Menu>
)

data class Menu (
    val category: String,
    val items: List<Item>
)

data class Item (
    val itemName: String,
    val price: Long,
    val image: String
)