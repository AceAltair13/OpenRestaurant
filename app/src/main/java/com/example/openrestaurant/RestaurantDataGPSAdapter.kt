package com.example.openrestaurant

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RestaurantDataGPSAdapter(private val listener: RestaurantDataGPSItemClicked) :
    RecyclerView.Adapter<RestaurantDataGPSHolder>() {
    private val items: ArrayList<RestaurantDataGPS> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDataGPSHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_data_gps_item, parent, false)
        val viewHolder = RestaurantDataGPSHolder(view)
        view.setOnClickListener {
            listener.onClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantDataGPSHolder, position: Int) {
        val currentItem = items[position]
        holder.titleView.text = currentItem.name
        val results = FloatArray(1)

        // TODO: Add actual location referencing
        currentItem.location?.latitude?.let {
            currentItem.location?.longitude?.let { it1 ->
                Location.distanceBetween(
                    it,
                    it1, 19.20505823884238, 72.85147471308453, results
                )
            }
        }
        Log.d("Location Data", "${results[0]}")
        holder.distance.text = "About ${results[0].toInt()}m away"
    }

    fun updateRestaurantDataGPS(updatedRestaurantDataGPS: ArrayList<RestaurantDataGPS>) {
        items.clear()
        items.addAll(updatedRestaurantDataGPS)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class RestaurantDataGPSHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById<TextView>(R.id.restaurantDataGPSItemTitle)
    val distance: TextView = itemView.findViewById<TextView>(R.id.restaurantDataGPSItemDistance)
}

interface RestaurantDataGPSItemClicked {
    fun onClicked(item: RestaurantDataGPS)
}