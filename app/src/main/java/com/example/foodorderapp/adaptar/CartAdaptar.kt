package com.example.foodorderapp.adaptar

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapp.databinding.CartItemBinding
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdaptar(

    // may be this order is also important changing order effects output
    //firstly i put images before description and when i extract image url it basically given description text
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemsPrice: MutableList<String>,
    private var cartDescriptions: MutableList<String>,
    private var cartImages: MutableList<String>,

    private var cartQuantity: MutableList<Int>
) : RecyclerView.Adapter<CartAdaptar.CartViewHolder>() {
    //instance firebase
    private val auth = FirebaseAuth.getInstance()

    init {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userId: String = auth.currentUser?.uid?:""
        val cartItemNumber: Int = cartItems.size

        itemQuantities = IntArray(cartItemNumber){1}
        cartItemsReference = database.reference.child("user").child(userId).child("CartItems")
    }

    companion object{
        private var itemQuantities : IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }

   // private val itemquantities = IntArray(cartItems.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                CartFoodName.text = cartItems[position]
                cartFoodPrice.text = cartItemsPrice[position]

                //load image
                val uriString = cartImages[position]
                Log.d("image","food Url: $uriString")
                val uri:Uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)

                cartItemQuantity.text = quantity.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }

            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val positionRetrieve:Int = position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey->
                if(uniqueKey!=null){
                    removeItem(position,uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey!= null){
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImages.removeAt(position)
                    cartDescriptions.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartItemsPrice.removeAt(position)
                    Toast.makeText(context,"Item Deleted Successfully",Toast.LENGTH_SHORT).show()

                    //update item quantity
                    itemQuantities = itemQuantities.filterIndexed { index, i ->index!=position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to Delete",Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int,onComplete:(String?)->Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                //loop for snapshot children
                    snapshot.children.forEachIndexed{index, dataSnapshot->
                        if(index == positionRetrieve){
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


}