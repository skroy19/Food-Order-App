package com.example.foodorderapp.adaptar

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

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if(itemPosition != RecyclerView.NO_POSITION){
                        deleteItem(position)
                    }
                }

            }
        }

        private fun decreaseQuantity(position: Int){
            if(itemquantities[position]>1){
                itemquantities[position]--
                binding.cartItemQuantity.text = itemquantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int){
            if(itemquantities[position]<10){
                itemquantities[position]++
                binding.cartItemQuantity.text = itemquantities[position].toString()
            }
        }

        fun deleteItem(position: Int){
            cartItems.removeAt(position)
            cartItemsPrice.removeAt(position)
            cartImages.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,cartItems.size)
        }

    }
}