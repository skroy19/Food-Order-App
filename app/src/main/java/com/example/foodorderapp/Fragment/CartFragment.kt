package com.example.foodorderapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapp.Model.CartItems
import com.example.foodorderapp.adaptar.CartAdaptar
import com.example.foodorderapp.PayOutActivity
import com.example.foodorderapp.R
import com.example.foodorderapp.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImageUri: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdaptar
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false)


        //initialize firebase
        auth = FirebaseAuth.getInstance()

        retrieveItems()

        binding.proceedButton.setOnClickListener{
            // get order items details before proceeding to check out
            getOrderItemsDetail()
//            val intent = Intent(requireContext(),PayOutActivity::class.java)
//            startActivity(intent)
        }

        return binding.root
    }

    private fun getOrderItemsDetail() {
        val orderIdReference: DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        val foodName : MutableList<String> = mutableListOf<String>()
        val foodPrice: MutableList<String> = mutableListOf<String>()
        val foodImage: MutableList<String> = mutableListOf<String>()
        val foodDescription: MutableList<String> = mutableListOf<String>()

        //get items quantities
        val foodQuantities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshoot in snapshot.children){
                    //get the cartItems to respective list
                    val orderItems = foodSnapshoot.getValue(CartItems::class.java)

                    //add items details into list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }

                }
                orderNow(foodName,foodPrice,foodDescription,foodImage,foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Order Making Failed. Please Try Again",Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        if(isAdded && context != null){
            val intent = Intent(requireContext(),PayOutActivity::class.java)
            intent.putExtra("FoodItemName", foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrive", foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
            intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemQuantities", foodQuantities as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveItems() {
        //database reference to the firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val foodReference: DatabaseReference= database.reference.child("user").child(userId).child("CartItems")

        //list to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImageUri = mutableListOf()
        quantity = mutableListOf()

        //fetch data from database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    //get the cartItems object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    //add cart items details to the list
                    cartItems?.foodName?.let{foodNames.add(it)}
                    cartItems?.foodPrice?.let{foodPrices.add(it)}
                    cartItems?.foodDescription?.let{foodDescriptions.add(it)}
                    cartItems?.foodImage?.let{foodImageUri.add(it)}
                    cartItems?.foodQuantity?.let{quantity.add(it)}
                }

                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdaptar(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodDescriptions,
                    foodImageUri,
                    quantity)
                binding.cardRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cardRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Data not fetched",Toast.LENGTH_SHORT).show( )
            }

        })
    }
}