package com.example.openrestaurant.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openrestaurant.R
import com.example.openrestaurant.model.Item

class RestaurantItemAdapter(private val listener: RestaurantItemClicked) :
    RecyclerView.Adapter<RestaurantItemHolder>() {
    private val items: ArrayList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_menu_items_item, parent, false)
        val viewHolder = RestaurantItemHolder(view)
        view.setOnClickListener {
            listener.onClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantItemHolder, position: Int) {
        val currentItem = items[position]
        holder.itemName.text = currentItem.item_name
        holder.itemPrice.text = "Rs ${currentItem.item_price}"
        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .placeholder(R.drawable.ic_baseline_fastfood_24)
            .into(holder.itemImage)
    }

    fun updateRestaurantItem(restaurantItem: ArrayList<Item>) {
        items.clear()
        items.addAll(restaurantItem)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class RestaurantItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemName: TextView = itemView.findViewById(R.id.restaurantMenuItemsItemTitle)
    val itemPrice: TextView = itemView.findViewById(R.id.restaurantMenuItemsItemPrice)
    val itemImage: ImageView = itemView.findViewById(R.id.restaurantMenuItemsItemImage)
}

interface RestaurantItemClicked {
    fun onClicked(item: Item)
}