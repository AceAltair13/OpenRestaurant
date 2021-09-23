package com.example.openrestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.openrestaurant.R
import com.example.openrestaurant.model.CartItem
import com.example.openrestaurant.paperdb.OrderCart

class RestaurantFinalItemsAdapter : RecyclerView.Adapter<RestaurantFinalItemHolder>() {

    private val cartItems = OrderCart.getCart()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantFinalItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_final_order_item, parent, false)
        return RestaurantFinalItemHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantFinalItemHolder, position: Int) {
        val currentItem = cartItems[position]
        val quantity = currentItem.quantity
        val price = currentItem.item.item_price
        val itemName = currentItem.item.item_name
        holder.itemName.text = "$quantity x $itemName"
        holder.itemPrice.text = "₹ ${quantity * price!!}"
    }

    fun updateFinalItems(newCartItems: MutableList<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

}

class RestaurantFinalItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemName = itemView.findViewById<TextView>(R.id.finalItemText)
    var itemPrice = itemView.findViewById<TextView>(R.id.finalItemPrice)
}