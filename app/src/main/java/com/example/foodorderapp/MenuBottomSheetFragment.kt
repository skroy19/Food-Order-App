package com.example.foodorderapp

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapp.Model.MenuItem
import com.example.foodorderapp.adaptar.MenuAdaptar
import com.example.foodorderapp.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)

        binding.buttonBack.setOnClickListener {
            dismiss()
        }

//        val menuFoodName = listOf("Burger","Sandwich","Momo","Pasta","Chicken","Chawmin")
//        val menuPrice = listOf("$5","$7","$4","$6","$8","$10")
//        val menuImage = listOf(
//            R.drawable.menu1,
//            R.drawable.menu2,
//            R.drawable.menu3,
//            R.drawable.menu4,
//            R.drawable.menu4,
//            R.drawable.menu2
//        )

        retrieveMenuItems()
        //val adapter = MenuAdaptar(ArrayList(menuFoodName),ArrayList(menuPrice),ArrayList(menuImage),requireContext())


        return binding.root

    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem: MenuItem? = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let{menuItems.add(it)}
                }
                Log.d("item","onDataChange: Data received")
                //Once data received set to adapter
                setAdapter()
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun setAdapter() {
        if(menuItems.isNotEmpty()){
            val adapter = MenuAdaptar(menuItems, requireContext())
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter
            Log.d("item","setAdapter: data set done")
        }
        else{
            Log.d("item","setAdapter: data not set")
        }

    }
    companion object {

    }

}