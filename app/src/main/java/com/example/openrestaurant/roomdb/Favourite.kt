package com.example.openrestaurant.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
class Favourite(
    @PrimaryKey @ColumnInfo(name = "order_id") val order_id: String,
    @ColumnInfo(name = "name") val favourite_name: String,
)