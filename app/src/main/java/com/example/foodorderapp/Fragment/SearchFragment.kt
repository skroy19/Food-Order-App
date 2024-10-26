package com.example.foodorderapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapp.R
import com.example.foodorderapp.adaptar.MenuAdaptar
import com.example.foodorderapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {


    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdaptar

    private val originalMenuFoodName = listOf("Burger","Sandwich","Momo","Pasta","Chicken","Chawmin")
    private val originalMenuItemPrice = listOf("$5","$7","$4","$6","$8","$10")
    private val originalMenuImage = listOf(
        R.drawable.menu1,
        R.drawable.menu2,
        R.drawable.menu3,
        R.drawable.menu4,
        R.drawable.menu4,
        R.drawable.menu2
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuItemPrice = mutableListOf<String>()
    private val filteredMenuImage = mutableListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        //adapter = MenuAdaptar(filteredMenuFoodName,filteredMenuItemPrice,filteredMenuImage,requireContext())
        binding.menuRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter=adapter

        //setup for search view
        setupSearchView()

        //show all menu items
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuItemPrice)
        filteredMenuImage.addAll(originalMenuImage)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })

    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()


        originalMenuFoodName.forEachIndexed{index,foodName ->
            if(foodName.contains(query.toString(), ignoreCase = true)){
                filteredMenuFoodName.add(foodName)
                filteredMenuItemPrice.add(originalMenuItemPrice[index])
                filteredMenuImage.add(originalMenuImage[index])
            }

        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}