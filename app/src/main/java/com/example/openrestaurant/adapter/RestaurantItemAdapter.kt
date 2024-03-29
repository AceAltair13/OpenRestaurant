package com.example.openrestaurant.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.openrestaurant.R
import com.example.openrestaurant.model.CartItem
import com.example.openrestaurant.model.Item
import com.example.openrestaurant.paperdb.OrderCart
import io.paperdb.Paper

class RestaurantItemAdapter :
    RecyclerView.Adapter<RestaurantItemHolder>() {
    private val items: ArrayList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_menu_items_item, parent, false)
        return RestaurantItemHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantItemHolder, position: Int) {
        val currentItem = items[position]

        val rid = Paper.book().read<String>("RESTAURANT_ID")
        val menuId = Paper.book().read<String>("MENU_ID")
        val itemPath = "$rid/menu/$menuId/items/${currentItem.id}"
        val item = CartItem(currentItem, itemPath)

        holder.itemName.text = currentItem.item_name
        holder.itemPrice.text = "Rs ${currentItem.item_price}"
        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .placeholder(R.drawable.ic_baseline_fastfood_24)
            .into(holder.itemImage)
        holder.itemCount.text = OrderCart.itemCount(item).toString()
        holder.itemPlusBtn.setOnClickListener {
            val item = CartItem(currentItem, itemPath)
            OrderCart.addItem(item)
            holder.itemCount.text = "${Integer.parseInt(holder.itemCount.text.toString()) + 1}"
        }
        holder.itemMinusBtn.setOnClickListener {
            val item = CartItem(currentItem, itemPath)
            OrderCart.removeItem(item)
            var count = Integer.parseInt(holder.itemCount.text.toString())
            if (count > 0) {
                count -= 1
            } else {
                count = 0
            }
            holder.itemCount.text = "$count"
        }
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
    var itemCount: TextView = itemView.findViewById(R.id.itemCount)
    val itemPlusBtn: ImageButton = itemView.findViewById(R.id.btnItemPlus)
    val itemMinusBtn: ImageButton = itemView.findViewById(R.id.btnItemMinus)
}