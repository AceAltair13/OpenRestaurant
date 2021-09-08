package com.example.openrestaurant.model

import java.io.Serializable

data class Menu(
    var category: String? = "",
    var items: ArrayList<Item>? = ArrayList()
) : Serializable