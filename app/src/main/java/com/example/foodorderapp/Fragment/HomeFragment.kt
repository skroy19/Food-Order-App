package com.example.foodorderapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodorderapp.MenuBottomSheetFragment
import com.example.foodorderapp.Model.MenuItem

import com.example.foodorderapp.R
import com.example.foodorderapp.adaptar.MenuAdaptar
import com.example.foodorderapp.adaptar.PopularAdaptar
import com.example.foodorderapp.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomShetDialog = MenuBottomSheetFragment()
            bottomShetDialog.show(parentFragmentManager, "Testing ")
        }
        //Retrieve and display popular menu
        retrieveAndDisplayPopularItems()

        return binding.root




    }

    private fun retrieveAndDisplayPopularItems() {
        //get reference to the databasee
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()


        //retrieve menu items from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                //display a random popular item
                randomPopularItems()
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun randomPopularItems() {
        //create a shuffled list of menu item
        val index:List<Int> = menuItems.indices.toList().shuffled()
        val numItemToShow = 5
        val subSetMenuItems: List<MenuItem> = index.take(numItemToShow).map { menuItems[it] }


        setPopularItemAdapter(subSetMenuItems)
    }

    private fun setPopularItemAdapter(subSetMenuItems: List<MenuItem>) {
        val adapter = MenuAdaptar(subSetMenuItems ,requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image is no $position"

                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })




    }


}