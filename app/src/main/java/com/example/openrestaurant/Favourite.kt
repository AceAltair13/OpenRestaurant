package com.example.openrestaurant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
class Favourite(@PrimaryKey @ColumnInfo(name = "order_id") val order_id: String,
                @ColumnInfo(name = "price") val price: Int
)