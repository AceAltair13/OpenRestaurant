package com.example.openrestaurant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantMenuAdapter(private val listener: RestaurantCategoryItemClicked): RecyclerView.Adapter<RestaurantMenuHolder>() {
    private val items: ArrayList<Menu> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_menu_category_item, parent, false)
        val viewHolder = RestaurantMenuHolder(view)
        view.setOnClickListener {
            listener.onClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantMenuHolder, position: Int) {
        val currentItem = items[position]
        holder.categoryName.text = currentItem.category
        holder.categoryCount.text = "${currentItem.items?.size} items"
    }

    fun updateRestaurantMenu(restaurantMenu: ArrayList<Menu>) {
        items.clear()
        items.addAll(restaurantMenu)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class RestaurantMenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val categoryName: TextView = itemView.findViewById(R.id.restaurantCategoryTitle)
    val categoryCount: TextView = itemView.findViewById(R.id.restaurantCategoryItemCount)
}

interface RestaurantCategoryItemClicked {
    fun onClicked(item: Menu)
}