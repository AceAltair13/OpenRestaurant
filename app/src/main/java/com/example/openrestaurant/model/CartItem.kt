package com.example.openrestaurant.model

data class CartItem(
    var item: Item,
    var itemPath: String,
    var quantity: Long = 0,
)