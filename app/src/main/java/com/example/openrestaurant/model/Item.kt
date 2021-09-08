package com.example.openrestaurant.model

import java.io.Serializable

data class Item(
    var item_name: String? = "",
    var item_price: Int? = 0,
    var image: String? = ""
) : Serializable