package com.example.openrestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.roomdb.Favourite

class FavouriteOrderAdapter(private val listener: FavouriteItemOnClicked) :
    RecyclerView.Adapter<FavouriteOrderViewHolder>() {
    private val items: ArrayList<Favourite> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_favourite_order_item, parent, false)
        val viewHolder = FavouriteOrderViewHolder(view)
        view.setOnClickListener {
            listener.onClicked(items[viewHolder.adapterPosition])
        }
        viewHolder.btnDeleteFavourite.setOnClickListener {
            listener.onDelete(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FavouriteOrderViewHolder, position: Int) {
        val currentItem = items[position]
        holder.favouriteLabel.text = currentItem.favourite_name
        holder.favouriteId.text = "Order ID : ${currentItem.order_id}"
    }

    fun updateFavouriteOrders(favouriteOrders: List<Favourite>) {
        items.clear()
        items.addAll(favouriteOrders)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class FavouriteOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val favouriteLabel: TextView = itemView.findViewById(R.id.favouriteItemOrderName)
    val favouriteId: TextView = itemView.findViewById(R.id.favouriteItemOrderId)
    val btnDeleteFavourite: ImageView = itemView.findViewById(R.id.btnDeleteFavourite)
}

interface FavouriteItemOnClicked {
    fun onClicked(favourite: Favourite)
    fun onDelete(favourite: Favourite)
}