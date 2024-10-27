package com.example.foodorderapp.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderapp.Model.OrderDetails
import com.example.foodorderapp.R
import com.example.foodorderapp.adaptar.BuyAgainAdapter
import com.example.foodorderapp.databinding.FragmentHistoryBinding
import com.example.foodorderapp.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)

        //Retrieve and display the user order history
        retrieveBuyHistory()

        binding.recentBuyItem.setOnClickListener {
            seeItemsRecentBuy()
        }

        return binding.root
    }

    //function to see items recent buy
    private fun seeItemsRecentBuy() {
        listOfOrderItem.firstOrNull()?.let { recentBuy->
            val intent = Intent(requireContext(),recentOrderItems::class.java)
            intent.putExtra("recentBuyOrderItem",recentBuy)
            startActivity(intent)
        }
    }

    //function to retrieve items buy history
    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""

        val buyItemReference: DatabaseReference = database.reference.child("user").child(userId).child("BuyHistory")
        //ensures recent buy
        val shortingQuery: Query = buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if(listOfOrderItem.isNotEmpty()){
                    //display the most recent order history
                    setDataInRecentBuyItem()
                    //setup the recyclerview with previoous order details
                    setPreviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //function to disply the most recent order details
    private fun setDataInRecentBuyItem() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                buyAgainFoodName.text = it.foodNames?.firstOrNull()?:""
                buyAgainFoodPrice.text = it.foodPrices?.firstOrNull()?:""
                val image = it.foodImages?.firstOrNull()?:""
                val uri= Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)

//                listOfOrderItem.reverse()
//                if(listOfOrderItem.isNotEmpty()){
//
//                }
            }
        }
    }

    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName:MutableList<String> = mutableListOf<String>()
        val buyAgainFoodPrice:MutableList<String> = mutableListOf<String>()
        val buyAgainFoodImage:MutableList<String> = mutableListOf<String>()

        for(i in 1 until  listOfOrderItem.size){
            listOfOrderItem[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOrderItem[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOrderItem[i].foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it) }
        }
        val rv = binding.buyAgainRecyclerView
        rv.layoutManager = LinearLayoutManager(requireContext())
        buyAgainAdapter = BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
        rv.adapter = buyAgainAdapter
    }


}