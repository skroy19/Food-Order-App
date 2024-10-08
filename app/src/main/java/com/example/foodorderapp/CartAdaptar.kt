package com.example.foodorderapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.databinding.CartItemBinding

class CartAdaptar(private val cartItems: MutableList<String>,private val cartItemsPrice:MutableList<String>,private var cartImages:MutableList<Int>) : RecyclerView.Adapter<CartAdaptar.CartViewHolder>() {

private val itemquantities = IntArray(cartItems.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }



    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder (private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemquantities[position]
                CartFoodName.text = cartItems[position]
                cartFoodPrice.text = cartItemsPrice[position]
                cartImage.setImageResource(cartImages[position])
                cartItemQuantity.text = quantity.toString()
            }
        }

    }
}