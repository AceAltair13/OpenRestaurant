package com.example.openrestaurant.paperdb

import com.example.openrestaurant.model.CartItem
import io.paperdb.Paper

class OrderCart {

    companion object {
        fun addItem(cartItem: CartItem) {
            val cart = getCart()

            val targetItem = cart.singleOrNull { it.item.id == cartItem.item.id }
            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {
                targetItem.quantity++
            }
            saveCart(cart)
        }

        fun removeItem(cartItem: CartItem) {
            val cart = getCart()

            val targetItem = cart.singleOrNull { it.item.id == cartItem.item.id }
            if (targetItem != null) {
                if (targetItem.quantity > 1) {
                    targetItem.quantity--
                } else {
                    cart.remove(targetItem)
                }
            }

            saveCart(cart)
        }

        fun itemCount(cartItem: CartItem): Long {
            val cart = getCart()
            val targetItem = cart.singleOrNull { it.item.id == cartItem.item.id }
            return targetItem?.quantity ?: 0
        }

        private fun saveCart(cart: MutableList<CartItem>) {
            Paper.book().write("cart", cart)
        }

        fun getCart(): MutableList<CartItem> {
            return Paper.book().read("cart", mutableListOf())
        }

        fun getOrderCartSize(): Long {
            var cartSize: Long = 0
            getCart().forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }

        fun getTotalCost(): Long {
            var cart = getCart()
            var cost: Long = 0
            for (item in cart) {
                cost += (item.item.item_price!! * item.quantity)
            }
            return cost
        }
    }

}